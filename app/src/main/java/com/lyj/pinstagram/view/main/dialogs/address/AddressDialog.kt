package com.lyj.pinstagram.view.main.dialogs.address

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.lyj.data.network.ApiBase
import com.lyj.core.base.BaseDialog
import com.lyj.core.extension.android.customScope
import com.lyj.core.extension.android.resDimen
import com.lyj.core.extension.lang.testTag
import com.lyj.core.rx.EndPoint
import com.lyj.core.rx.EntryPoint
import com.lyj.core.rx.at
import com.lyj.core.rx.plusAssign
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogAddressBinding
import com.lyj.pinstagram.view.main.SetCurrentLocation
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@AndroidEntryPoint
class AddressDialog(val setCurrentLocation: SetCurrentLocation) :
    BaseDialog<AddressDialogViewModel,DialogAddressBinding>({ layoutInflater, viewGroup, _ ->
        DialogAddressBinding.inflate(layoutInflater, viewGroup, false)
    }) {
    override val viewModel: AddressDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopContainerSize(
            resDimen(R.dimen.address_dialog_horizontal_margin),
            resDimen(R.dimen.address_dialog_vertical_margin)
        )
        binding.dialogAddressWebView.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            webChromeClient = WebChromeClient()
            addJavascriptInterface(AndroidBridge(), "Pinstagram")
            loadUrl(ApiBase.ADDRESS_URL)
        }
    }


    private fun setTopContainerSize(horizontalMargin: Float, verticalMargin: Float) {
        binding.dialogAddressContainer.layoutParams.apply {
            width = (viewModel.pxWidth - horizontalMargin).toInt()
            height = (viewModel.pxHeight - verticalMargin).toInt()
        }
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(zoneCode: String?, address: String?, buildingName: String?) {
            Log.d(testTag, "$zoneCode $address $buildingName")

            if (address != null) {
                customScope(EntryPoint.START.runImmediately() at EndPoint.STOP) += {
                    viewModel
                        .requestGeocoding(address)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            Toast.makeText(
                                requireContext(),
                                R.string.address_dialog_request_start,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .subscribe({
                            if (it.isOk && it.data != null) {
                                setCurrentLocation(
                                    it.data!!.latitude,
                                    it.data!!.longitude
                                )
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.address_dialog_request_fail,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            dismiss()
                        }, {
                            Toast.makeText(
                                requireContext(),
                                R.string.address_dialog_request_fail,
                                Toast.LENGTH_LONG
                            ).show()
                            it.printStackTrace()
                            dismiss()
                        })
                }
            }
        }
    }
}