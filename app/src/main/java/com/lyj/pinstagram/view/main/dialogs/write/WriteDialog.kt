package com.lyj.pinstagram.view.main.dialogs.write

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.itemSelections
import com.lyj.core.base.BaseDialog
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.lang.testTag
import com.lyj.customui.dialog.edittext.ErrorMessage
import com.lyj.customui.dialog.edittext.IsValidated
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogWriteBinding
import com.lyj.pinstagram.lifecycle.MapLifeCycle
import gun0912.tedbottompicker.TedBottomPicker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit


class WriteDialog(private val viewModel: WriteDialogViewModel) :
    BaseDialog<DialogWriteBinding>(
        viewModel,
        { inflater, viewGroup, _ -> DialogWriteBinding.inflate(inflater, viewGroup, false) }
    ),
    View.OnClickListener,
    OnMapReadyCallback {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopContainerSize(
            viewModel.getDimen(R.dimen.write_dialog_horizontal_margin),
            viewModel.getDimen(R.dimen.permission_dialog_container_vertical_margin)
        )
        bindMapView()
        viewModel.viewDisposable += bindObservable()
    }

    private fun setTopContainerSize(horizontalMargin: Float, verticalMargin: Float) {
        binding.writeDialogContainer.layoutParams.apply {
            width = (viewModel.pxWidth - horizontalMargin).toInt()
            height = (viewModel.pxHeight - verticalMargin).toInt()
        }
    }

    private fun bindMapView() {
        binding.writeMapView.getMapAsync(this)
        MapLifeCycle(viewModel.context.lifecycle, binding.writeMapView)
    }

    override fun onClick(v: View) {
        dismiss()
    }

    //    private fun bindBtnSend() : DisposableFunction = {
//        binding
//            .writeBtnSend
//            .clicks()
//            .filter { viewModel.currentAsset != null }
//            .throttleFirst(1,TimeUnit.SECONDS)
//            .observeOn(Schedulers.io())
//            .flatMap {
//                binding.writeProgressLayout.visibility = View.VISIBLE
//                viewModel
//                    .requestUpload(viewModel.currentAsset!!.uri).toObservable()
//            }.flatMap {
////                viewModel
//
//            }
//            .subscribe({
//
//            },{
//                Toast.makeText(viewModel.context,R.string.write_toast_create_fail,Toast.LENGTH_LONG).show()
//                it.printStackTrace()
//            })
//    }
    private fun bindObservable(): DisposableFunction = {
        Observable.combineLatest(
            bindTitleText(),
            bindDesciptionText(),
            bindSpinner(),
            bindImageButton(),
        ) { titlePair, desciptionPair, selectedItem, imageUri ->
            WriteRequestAsset(titlePair, desciptionPair, selectedItem, imageUri)
        }.subscribe({
            val isValidated = it.title.first && it.description.first
            binding.writeBtnSend.isEnabled = isValidated
            viewModel.currentAsset = if (isValidated) it else null
        }, {
            it.printStackTrace()
        })
    }


    private fun bindSpinner(): Observable<ContentsTagType> {
        binding.writeTagSpinner.apply {
            adapter = ArrayAdapter<String>(
                viewModel.context,
                android.R.layout.simple_spinner_item,
                viewModel.spinnerItems.map { viewModel.getString(it.kor) })
        }
        return binding.writeTagSpinner.itemSelections().map { viewModel.spinnerItems[it] }
    }

    private fun bindImageButton(): Observable<Uri> =
        binding
            .writeImageButton
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable.create<Uri> { emitter ->
                    TedBottomPicker
                        .with(viewModel.context) // Rxjava3을 지원하지 않아 이렇게 사용
                        .show {
                            Log.d(testTag, it.toString())
                            Log.d(testTag, it.path ?: "")
                            emitter.onNext(it)
                        }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                binding.writeImageButton.scaleType = ImageView.ScaleType.CENTER_INSIDE

                Glide
                    .with(viewModel.context)
                    .load(it)
                    .into(binding.writeImageButton)
                it
            }


    private fun bindTitleText(): Observable<Pair<IsValidated, ErrorMessage?>> =
        binding.writeTextTitle.bindRule(viewModel.titleRule)

    private fun bindDesciptionText(): Observable<Pair<IsValidated, ErrorMessage?>> =
        binding.writeTextDescription.bindRule(viewModel.descriptionRule)

    override fun onMapReady(map: GoogleMap) {
        viewModel
            .getUserLocationOnce(viewModel.context)
            ?.subscribe({
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.latitude,
                            it.longitude
                        ), 16.5f
                    )
                )
            }, {
                it.printStackTrace()
            })

        map
            .setOnCameraMoveListener {
                if (!binding.writePinAnimationView.isAnimating) binding.writePinAnimationView.playAnimation()
            }

        map
            .setOnCameraIdleListener {
                binding.writePinAnimationView.progress = 0f
                binding.writePinAnimationView.pauseAnimation()
            }
    }
}