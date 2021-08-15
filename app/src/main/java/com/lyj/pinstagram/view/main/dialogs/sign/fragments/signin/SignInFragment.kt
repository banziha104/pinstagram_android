package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signin

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.android.resString
import com.lyj.core.extension.lang.plusAssign
import com.lyj.domain.base.ApiResponseCode
import com.lyj.domain.network.auth.request.SignInRequest
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.SignInFragmentBinding
import com.lyj.pinstagram.extension.lang.observable
import com.lyj.pinstagram.view.main.dialogs.sign.ChangeViewTypeCallBack
import com.lyj.pinstagram.view.main.dialogs.sign.SignViewType
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignInFragment private constructor(
    private val changeViewTypeCallBack: ChangeViewTypeCallBack,
    private val dismiss: () -> Unit
) :
    BaseFragment<SignInFragmentViewModel, SignInFragmentBinding>(
        R.layout.sign_in_fragment,
        { layoutInflater, viewGroup ->
            SignInFragmentBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }
    ) {

    companion object {
        private val instance: SignInFragment? = null
        fun getInstance(change: ChangeViewTypeCallBack, dismiss: () -> Unit): SignInFragment =
            instance ?: SignInFragment(change, dismiss)
    }


    override val viewModel: SignInFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDisposables += bindBtnToSignUp()
        viewDisposables += bindEditText()
        viewDisposables += bindBtnSend()
    }

    private fun bindBtnSend(): DisposableFunction = {
        binding.signInBtnSend
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .doOnNext { binding.signInProgress.visibility = View.VISIBLE }
            .flatMapSingle {
                viewModel.requestSignIn(
                    SignInRequest(
                        binding.signInEditEmail.getText(),
                        binding.signInEditPassword.getText()
                    )
                )
            }.flatMap { // TODO : FlatMapCompletable 이 정상동작하지 않음. 원인 파악후 수정
                val type = it.getCodeType()
                if (type == ApiResponseCode.USER_NOT_FOUNDED || type == ApiResponseCode.PASSWORD_NOT_CORRECT || type == ApiResponseCode.NO_CONTENTS) {
                    SignInRequestResult.USER_NOT_FOUNDED.observable()
                }else {
                    if (it.isOk && it.data?.token != null) {
                        viewModel.saveToken(it.data!!.token)
                            .andThen(SignInRequestResult.SUCCESS.observable())
                    } else {
                        SignInRequestResult.DATABASE_FAIL.observable()
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.signInProgress.visibility = View.GONE
                when (it) {
                    SignInRequestResult.SUCCESS -> makeToast(R.string.sign_in_request_success,true)
                    SignInRequestResult.USER_NOT_FOUNDED -> makeToast(R.string.sign_in_user_not_founded,false)
                    SignInRequestResult.DATABASE_FAIL -> makeToast(R.string.sign_in_database_fail,false)
                }
            }, {
                makeToast(R.string.sign_up_request_fail,true)
                binding.signInProgress.visibility = View.GONE
                it.printStackTrace()
            })
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

    private fun bindEditText(): DisposableFunction = {
        Observable.combineLatest(
            binding.signInEditEmail.bindRule(viewModel.emailRule),
            binding.signInEditPassword.bindRule(viewModel.passwordRule)
        ) { email, password -> email to password }
            .subscribe({ (email, password) ->
                binding.signInBtnSend.isEnabled = email.first && password.first
            }, {
                it.printStackTrace()
            })
    }

    private fun bindBtnToSignUp(): DisposableFunction = {
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
                }
        }
    }
}

enum class SignInRequestResult {
    SUCCESS, USER_NOT_FOUNDED, DATABASE_FAIL;
}
