package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.rx.DisposableLifecycleController
import com.lyj.core.rx.RxLifecycleObserver
import com.lyj.core.rx.disposedBy
import com.lyj.data.source.remote.entity.auth.request.SignUpRequest
import com.lyj.domain.model.network.ApiResponseCode
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.SignUpFragmentBinding
import com.lyj.pinstagram.extension.lang.observable
import com.lyj.pinstagram.view.ProgressController
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignUpFragment : Fragment(), ProgressController, DisposableLifecycleController {

    companion object {
        private val instance: SignUpFragment? = null
        fun getInstance(dismiss: () -> Unit): SignUpFragment =
            instance ?: SignUpFragment().apply {
                this.dismiss = dismiss
            }
    }
    private val viewModel: SignUpFragmentViewModel by viewModels()
    private lateinit var dismiss: () -> Unit
    private lateinit var binding : SignUpFragmentBinding

    override val progressLayout: View by lazy { binding.signUpProgress }
    override val disposableLifecycleObserver: RxLifecycleObserver = RxLifecycleObserver(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEditText()
        bindBtnSend()
    }

    private fun bindEditText(){
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
            .disposedBy(this)
    }

    private fun bindBtnSend(){
        binding
            .signUpBtnSend
            .clicks()
            .throttleFirst(1, TimeUnit.SECONDS)
            .doOnNext { showProgressLayout() }
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
                hideProgressLayout()
                when(it){
                    SignUpRequestResult.SUCCESS -> makeToast(R.string.sign_up_request_success,true)
                    SignUpRequestResult.ID_DUPLICATED ->  makeToast(R.string.sign_up_duplicated_email,false)
                    SignUpRequestResult.DATABASE_FAIL -> makeToast(R.string.sign_in_database_fail,false)
                }
            }, {
                hideProgressLayout()
                makeToast(R.string.sign_up_request_fail,true)
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
}

enum class SignUpRequestResult {
    SUCCESS,ID_DUPLICATED,DATABASE_FAIL
}

