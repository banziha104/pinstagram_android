package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signup

import android.app.admin.SystemUpdatePolicy
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.lang.plusAssign
import com.lyj.domain.network.auth.request.SignUpRequest
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.SignUpFragmentBinding
import com.lyj.pinstagram.view.main.dialogs.sign.ChangeViewTypeCallBack
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignUpFragment private constructor(val changeViewTypeCallBack: ChangeViewTypeCallBack) :
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
        fun getInstance(change: ChangeViewTypeCallBack): SignUpFragment =
            instance ?: SignUpFragment(change)
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
            .flatMap {
                binding.signUpProgress.visibility = View.VISIBLE
                viewModel.requestSignUp(
                    SignUpRequest(
                        binding.signUpEditEmail.getText()
                            ?: throw ValidationFailedException("email"),
                        binding.signUpEditPassword.getText()
                            ?: throw ValidationFailedException("password"),
                        binding.signUpEditName.getText() ?: throw ValidationFailedException("name")
                    )
                ).toObservable()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.signUpProgress.visibility = View.GONE

                if (it.isOk && it.data?.token != null) {

                }else{
                    Toast.makeText(requireContext(), R.string.sign_up_request_fail, Toast.LENGTH_LONG).show()
                }
            }, {
                binding.signUpProgress.visibility = View.GONE
                Toast.makeText(requireContext(), R.string.sign_up_request_fail, Toast.LENGTH_LONG).show()
                it.printStackTrace()
            })
    }
}

class ValidationFailedException(message: String) : RuntimeException("${message}의 형식이 맞지 않습니다")