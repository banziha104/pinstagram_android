package com.lyj.pinstagram.view.main.dialogs.write

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.lyj.api.database.dao.TokenIsNotValidated
import com.lyj.core.base.BaseDialog
import com.lyj.core.extension.android.fromResumeToPause
import com.lyj.core.extension.android.fromStartToStopScope
import com.lyj.core.extension.android.resDimen
import com.lyj.core.extension.android.resString
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.lang.testTag
import com.lyj.core.rx.DisposableFunction
import com.lyj.core.rx.plusAssign
import com.lyj.customui.dialog.edittext.ErrorMessage
import com.lyj.customui.dialog.edittext.IsValidated
import com.lyj.customui.dialog.edittext.ValidationFailedException
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.domain.network.contents.request.ContentsCreateRequest
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogWriteBinding
import com.lyj.pinstagram.extension.lang.bearerToken
import com.lyj.pinstagram.lifecycle.MapLifeCycle
import com.lyj.pinstagram.view.ProgressController
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class WriteDialog(private val currentLocation :LatLng) : BaseDialog<WriteDialogViewModel,DialogWriteBinding>(
    { inflater, viewGroup, _ -> DialogWriteBinding.inflate(inflater, viewGroup, false) }
),
    View.OnClickListener,
    OnMapReadyCallback,
    ProgressController {

    override val viewModel: WriteDialogViewModel by viewModels()

    override val progressLayout: View by lazy { binding.writeProgressLayout }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopContainerSize(
            resDimen(R.dimen.write_dialog_horizontal_margin),
            resDimen(R.dimen.permission_dialog_container_vertical_margin)
        )
        bindMapView()
        fromStartToStopScope += bindObservable()
        fromStartToStopScope += bindBtnSend()
    }

    private fun setTopContainerSize(horizontalMargin: Float, verticalMargin: Float) {
        binding.writeContainer.layoutParams.apply {
            width = (viewModel.pxWidth - horizontalMargin).toInt()
            height = (viewModel.pxHeight - verticalMargin).toInt()
        }
    }

    lateinit var googleMap: GoogleMap

    private fun bindMapView() {
        binding.writeMapView.getMapAsync(this)
        MapLifeCycle(lifecycle, binding.writeMapView)
    }

    override fun onClick(v: View) {
        dismiss()
    }

    private fun bindBtnSend(): DisposableFunction = {
        binding
            .writeBtnSend
            .clicks()
            .filter { viewModel.currentAsset != null }
            .throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { showProgressLayout() }
            .observeOn(Schedulers.io())
            .flatMap {
                viewModel
                    .requestUpload(viewModel.currentAsset!!.uri).toObservable()
            }.flatMap {
                Observable.combineLatest(
                    viewModel.getToken().toObservable(),
                    Observable.just(it)
                ) { a, b -> a to b }
            }
            .observeOn(AndroidSchedulers.mainThread()) // googleMap에 접근하기 위해 Main Thread
            .flatMap { (token, url) ->
                if (token.isEmpty() || token[0].token.isEmpty()) {
                    throw TokenIsNotValidated()
                }
                Observable.just(
                    CreateRequestData(
                        url, token[0].token.bearerToken(),
                        googleMap.cameraPosition.target.latitude,
                        googleMap.cameraPosition.target.longitude
                    )
                )
            }
            .observeOn(Schedulers.io())
            .flatMapSingle { (url, token, lat, lng) ->
                viewModel.requestCreateContents(
                    token,
                    ContentsCreateRequest(
                        binding.writeTxtTitle.getText(),
                        binding.writeTxtDescription.getText(),
                        url,
                        resString(viewModel.spinnerItems[binding.writeSpinner.selectedItemPosition].origin),
                        lat,
                        lng
                    )
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideProgressLayout()
                Toast.makeText(
                    requireContext(),
                    if (it.isOk) resString(R.string.write_toast_create_success) else it.message,
                    Toast.LENGTH_LONG
                ).show()
                dismiss()
            }, {
                hideProgressLayout()
                when (it) {
                    is ValidationFailedException, is TokenIsNotValidated ->
                        Toast.makeText(
                            viewModel.context,
                            it.message,
                            Toast.LENGTH_LONG
                        ).show()
                    else -> Toast.makeText(
                        viewModel.context,
                        R.string.write_toast_create_fail,
                        Toast.LENGTH_LONG
                    ).show()
                }
                dismiss()
                it.printStackTrace()
            })
    }

    private fun bindObservable(): DisposableFunction = {
        Observable.combineLatest(
            bindTitleText(),
            bindDesciptionText(),
            bindSpinner(),
            bindImageButton(),
        ) { titlePair, desciptionPair, selectedItem, image ->
            WriteRequestAsset(titlePair, desciptionPair, selectedItem, image)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val isValidated = it.title.first && it.description.first
                Log.d(testTag, "${it.toString()} ${isValidated}")
                binding.writeBtnSend.isEnabled = isValidated
                viewModel.currentAsset = if (isValidated) it else null
            }, {
                it.printStackTrace()
            })
    }


    private fun bindSpinner(): Observable<ContentsTagType> {
        binding.writeSpinner.apply {
            adapter = ArrayAdapter<String>(
                viewModel.context,
                android.R.layout.simple_spinner_item,
                viewModel.spinnerItems.map { resString(it.kor) })
        }

        return binding.writeSpinner
            .itemSelections()
            .map { viewModel.spinnerItems[it] }
            .startWith(Observable.just(viewModel.spinnerItems[0]))
    }

    private fun bindImageButton(): Observable<Uri> =
        binding
            .writeBtnImage
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable.create<Uri> { emitter ->
                    TedBottomPicker // Rxjava3을 지원하지 않아 이렇게 사용
                        .with(requireActivity())
                        .show {
                            emitter.onNext(it)
                        }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                binding.writeBtnImage.scaleType = ImageView.ScaleType.CENTER_INSIDE

                Glide
                    .with(viewModel.context)
                    .load(it)
                    .into(binding.writeBtnImage)
                it
            }


    private fun bindTitleText(): Observable<Pair<IsValidated, ErrorMessage?>> =
        binding.writeTxtTitle.bindRule(viewModel.titleRule)

    private fun bindDesciptionText(): Observable<Pair<IsValidated, ErrorMessage?>> =
        binding.writeTxtDescription.bindRule(viewModel.descriptionRule)

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        viewModel
            .getUserLocationOnce(requireActivity())
            ?.subscribe({
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLocation, 16.5f
                    )
                )
            }, {
                it.printStackTrace()
            })

        map
            .setOnCameraMoveListener {
                if (!binding.writeLottiePin.isAnimating) binding.writeLottiePin.playAnimation()
            }

        map
            .setOnCameraIdleListener {
                binding.writeLottiePin.progress = 0f
                binding.writeLottiePin.pauseAnimation()
            }
    }
}

data class CreateRequestData(
    val url: String,
    val token: String,
    val lat: Double,
    val lng: Double
)