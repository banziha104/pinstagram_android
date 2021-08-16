package com.lyj.pinstagram.view.main.dialogs.address

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.lyj.api.network.ApiBase
import com.lyj.core.base.BaseDialog
import com.lyj.core.extension.android.resDimen
import com.lyj.core.extension.lang.testTag
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogAddressBinding
import com.lyj.pinstagram.view.main.SetCurrentLocation
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@AndroidEntryPoint
class AddressDialog(val setCurrentLocation: SetCurrentLocation) :
    BaseDialog<DialogAddressBinding, AddressDialogViewModel>({ layoutInflater, viewGroup, _ ->
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
                viewModel
                    .requestGeocoding(address)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.isOk && it.data != null){
                            setCurrentLocation(
                                it.data!!.latitude,
                                it.data!!.longitude
                            )
                        }else{
                            Toast.makeText(requireContext(),R.string.address_dialog_request_fail,Toast.LENGTH_LONG).show()
                        }
                        dismiss()
                    }, {
                        Toast.makeText(requireContext(),R.string.address_dialog_request_fail,Toast.LENGTH_LONG).show()
                        it.printStackTrace()
                        dismiss()
                    })
            }
        }
    }
}