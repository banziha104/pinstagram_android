package com.lyj.pinstagram.view.main.dialog

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.base.BaseDialog
import com.lyj.core.extension.lang.plusAssign
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogWriteBinding
import gun0912.tedbottompicker.TedBottomPicker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.TimeUnit


class WriteDialog(private val viewModel: WriteDialogViewModel) :
    BaseDialog<DialogWriteBinding>(viewModel, { DialogWriteBinding.inflate(it) }),
    View.OnClickListener,
    OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackgroundSize(
            viewModel.getDimen(R.dimen.write_dialog_horizontal_margin),
            viewModel.getDimen(R.dimen.permission_dialog_container_vertical_margin)
        )
        bindEditText()
        bindImageButton()
    }

    private fun setBackgroundSize(horizontalMargin: Float, verticalMargin: Float) {
        binding.writeDialogContainer.layoutParams.apply {
            width = (viewModel.pxWidth - horizontalMargin).toInt()
            height = (viewModel.pxHeight - verticalMargin).toInt()
        }
    }

    private fun bindImageButton() {
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
                            emitter.onNext(it)
                        }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Glide
                    .with(viewModel.context)
                    .load(it)
                    .into(binding.writeImageButton)
            }
    }

    private fun bindEditText() {
        binding.writeTextDescription.bindRule(viewModel.descriptionRule)
            .subscribe { (isValidate, errorMessage) ->
            }
        binding.writeTextTitle.bindRule(viewModel.titleRule)
            .subscribe { (isValidate, errorMessage) -> }
    }

    override fun onClick(v: View) {
        dismiss()
    }

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
    }
}