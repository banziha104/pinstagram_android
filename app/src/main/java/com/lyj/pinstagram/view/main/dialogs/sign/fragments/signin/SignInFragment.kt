package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signin

import android.content.BroadcastReceiver
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.extension.base.resString
import com.lyj.core.rx.DisposableLifecycleController
import com.lyj.core.rx.RxLifecycleObserver
import com.lyj.core.rx.disposedBy
import com.lyj.data.source.local.entity.TOKEN_ID
import com.lyj.data.source.local.entity.TokenEntity
import com.lyj.data.source.remote.entity.auth.request.SignInRequest
import com.lyj.domain.model.network.ApiResponseCode
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.SignInFragmentBinding
import com.lyj.pinstagram.extension.lang.observable
import com.lyj.pinstagram.view.ProgressController
import com.lyj.pinstagram.view.main.dialogs.sign.ChangeViewTypeCallBack
import com.lyj.pinstagram.view.main.dialogs.sign.SignViewType
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.io.Serializable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignInFragment ():
    Fragment(), ProgressController, DisposableLifecycleController {


    companion object {
        private val instance: SignInFragment? = null
        fun getInstance(change: ChangeViewTypeCallBack, dismiss: () -> Unit): SignInFragment =
            instance ?: SignInFragment().apply {
                changeViewTypeCallBack = change
                this.dismiss = dismiss
            }
    }

    private val viewModel: SignInFragmentViewModel by viewModels()
    private lateinit var binding : SignInFragmentBinding
    private lateinit var changeViewTypeCallBack: ChangeViewTypeCallBack
    private lateinit var dismiss: () -> Unit
    override val progressLayout: View by lazy { binding.signInProgress }
    override val disposableLifecycleObserver: RxLifecycleObserver = RxLifecycleObserver(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignInFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindBtnToSignUp()
        bindEditText()
        bindBtnSend()
    }

    private fun bindBtnSend(){
        binding.signInBtnSend
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .doOnNext { showProgressLayout() }
            .flatMapSingle {
                viewModel
                    .requestSignInUseCase
                    .execute(
                        SignInRequest(
                            binding.signInEditEmail.getText(),
                            binding.signInEditPassword.getText()
                        )
                    )
            }.flatMap {
                val type = it.getCodeType()
                if (type == ApiResponseCode.USER_NOT_FOUNDED || type == ApiResponseCode.PASSWORD_NOT_CORRECT || type == ApiResponseCode.NO_CONTENTS) {
                    SignInRequestResult.USER_NOT_FOUNDED.observable()
                } else {
                    if (it.isOk && it.data?.token != null) {
                        viewModel
                            .insertTokenUseCase
                            .execute(
                                TokenEntity(TOKEN_ID, it.data!!.token)
                            )
                            .andThen(SignInRequestResult.SUCCESS.observable())
                    } else {
                        SignInRequestResult.DATABASE_FAIL.observable()
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideProgressLayout()
                when (it) {
                    SignInRequestResult.SUCCESS -> makeToast(R.string.sign_in_request_success, true)
                    SignInRequestResult.USER_NOT_FOUNDED -> makeToast(
                        R.string.sign_in_user_not_founded,
                        false
                    )
                    SignInRequestResult.DATABASE_FAIL -> makeToast(
                        R.string.sign_in_database_fail,
                        false
                    )
                }
            }, {
                makeToast(R.string.sign_up_request_fail, true)
                hideProgressLayout()
                it.printStackTrace()
            })
            .disposedBy(this)
    }

    private fun makeToast(@StringRes id: Int, isNeedDismiss: Boolean = false) {
        Toast
            .makeText(
                requireActivity(),
                id,
                Toast.LENGTH_LONG
            )
            .show()
        if (isNeedDismiss) dismiss()
    }

    private fun bindEditText(){
        Observable.combineLatest(
            binding.signInEditEmail.bindRule(viewModel.emailRule),
            binding.signInEditPassword.bindRule(viewModel.passwordRule)
        ) { email, password -> email to password }
            .subscribe({ (email, password) ->
                binding.signInBtnSend.isEnabled = email.first && password.first
            }, {
                it.printStackTrace()
            }).disposedBy(this)
    }

    private fun bindBtnToSignUp(){
        binding.signInBtnToSignUp.let { button ->
            button.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(
                    resString(R.string.sign_in_info_to_sign_up),
                    Html.FROM_HTML_MODE_LEGACY
                )
            } else {
                Html.fromHtml(resString(R.string.sign_in_info_to_sign_up))
            }
            button
                .clicks()
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    changeViewTypeCallBack(SignViewType.SIGN_UP)
                }.disposedBy(this)
        }
    }
}

enum class SignInRequestResult {
    SUCCESS, USER_NOT_FOUNDED, DATABASE_FAIL;
}

class A: Serializable{
}
