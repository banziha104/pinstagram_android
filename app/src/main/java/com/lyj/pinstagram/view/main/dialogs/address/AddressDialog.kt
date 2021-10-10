package com.lyj.pinstagram.view.main.dialogs.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.lyj.data.source.remote.http.ApiBase
import com.lyj.core.extension.base.resDimen
import com.lyj.core.extension.lang.testTag
import com.lyj.core.rx.DisposableLifecycleController
import com.lyj.core.rx.RxLifecycleObserver
import com.lyj.core.rx.disposeByOnDestory
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogAddressBinding
import com.lyj.pinstagram.view.main.SetCurrentLocation
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@AndroidEntryPoint
class AddressDialog(val setCurrentLocation: SetCurrentLocation): DialogFragment() , DisposableLifecycleController{
    private val viewModel: AddressDialogViewModel by viewModels()
    private lateinit var binding: DialogAddressBinding
    override val disposableLifecycleObserver: RxLifecycleObserver = RxLifecycleObserver(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                    .requestGeoCodeUseCase
                    .execute(address)
                    .disposeByOnDestory(this@AddressDialog)
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
