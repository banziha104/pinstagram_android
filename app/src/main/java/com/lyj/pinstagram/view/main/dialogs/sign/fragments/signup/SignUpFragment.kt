package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.lang.plusAssign
import com.lyj.domain.base.ApiResponseCode
import com.lyj.domain.network.auth.request.SignUpRequest
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.SignUpFragmentBinding
import com.lyj.pinstagram.extension.lang.observable
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignUpFragment private constructor(
    private val dismiss: () -> Unit
) :
    BaseFragment<SignUpFragmentViewModel, SignUpFragmentBinding>(
        R.layout.sign_up_fragment,
        { layoutInflater, viewGroup ->
            SignUpFragmentBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }
    ) {

    companion object {
        private val instance: SignUpFragment? = null
        fun getInstance(dismiss: () -> Unit): SignUpFragment =
            instance ?: SignUpFragment(dismiss)
    }

    override val viewModel: SignUpFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDisposables += bindEditText()
        viewDisposables += bindBtnSend()
    }

    private fun bindEditText(): DisposableFunction = {
        Observable
            .combineLatest(
                binding.signUpEditEmail.bindRule(viewModel.emailRule)
                    .map { (isValidated, _) -> isValidated },
                binding.signUpEditPassword.bindRule(viewModel.passWordRule)
                    .map { (isValidated, _) -> isValidated },
                binding.signUpEditName.bindRule(viewModel.nameRule)
                    .map { (isValidated, _) -> isValidated },
            ) { idValid, psVaild, nameVaild ->
                listOf(idValid, psVaild, nameVaild)
            }.subscribe({
                binding.signUpBtnSend.isEnabled = it.all { validated -> validated }
            }, {
                it.printStackTrace()
            })
    }

    private fun bindBtnSend(): DisposableFunction = {
        binding
            .signUpBtnSend
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .doOnNext { binding.signUpProgress.visibility = View.VISIBLE }
            .flatMapSingle {
                viewModel.requestSignUp(
                    SignUpRequest(
                        binding.signUpEditEmail.getText(),
                        binding.signUpEditPassword.getText(),
                        binding.signUpEditName.getText()
                    )
                )
            }
            .flatMap { // TODO : FlatMapCompletable 이 정상동작하지 않음. 원인 파악후 수정
                if (it.getCodeType() == ApiResponseCode.SIGNIN_DUPLICATION) {
                    SignUpRequestResult.ID_DUPLICATED.observable()
                } else {
                    if (it.isOk && it.data?.token != null) {
                        viewModel.saveToken(it.data!!.token).andThen(SignUpRequestResult.SUCCESS.observable())
                    } else {
                        SignUpRequestResult.DATABASE_FAIL.observable()
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.signUpProgress.visibility = View.GONE
                when(it){
                    SignUpRequestResult.SUCCESS -> makeToast(R.string.sign_up_request_success,true)
                    SignUpRequestResult.ID_DUPLICATED ->  makeToast(R.string.sign_up_duplicated_email,false)
                    SignUpRequestResult.DATABASE_FAIL -> makeToast(R.string.sign_in_database_fail,false)
                }
            }, {
                binding.signUpProgress.visibility = View.GONE
                makeToast(R.string.sign_up_request_fail,true)
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
}

enum class SignUpRequestResult {
    SUCCESS,ID_DUPLICATED,DATABASE_FAIL
}

