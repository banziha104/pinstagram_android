package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signin

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import com.iyeongjoon.nicname.core.rx.DisposableFunction
import com.jakewharton.rxbinding4.view.clicks
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.lang.plusAssign
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.SignInFragmentBinding
import com.lyj.pinstagram.view.main.dialogs.sign.ChangeViewTypeCallBack
import com.lyj.pinstagram.view.main.dialogs.sign.SignViewType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SignInFragment private constructor(val changeViewTypeCallBack : ChangeViewTypeCallBack) :
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
        private val instance : SignInFragment? = null
        fun getInstance(change: ChangeViewTypeCallBack) : SignInFragment = instance ?: SignInFragment(change)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDisposables += bindBtnToSignUp()
    }

    private fun bindBtnToSignUp() : DisposableFunction = {
        binding.signInBtnToSignUp.let { button ->
            button.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(
                    getString(R.string.sign_in_info_to_sign_up),
                    Html.FROM_HTML_MODE_LEGACY
                )
            } else {
                Html.fromHtml(getString(R.string.sign_in_info_to_sign_up))
            }
            button
                .clicks()
                .throttleFirst(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    changeViewTypeCallBack(SignViewType.SIGN_UP)
                }
        }
    }



    override val viewModel: SignInFragmentViewModel by viewModels()

}