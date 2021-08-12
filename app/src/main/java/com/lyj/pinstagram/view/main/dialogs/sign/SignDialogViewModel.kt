package com.lyj.pinstagram.view.main.dialogs.sign

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.lyj.core.base.DialogViewModel
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.pinstagram.view.main.dialogs.sign.fragments.signin.SignInFragment
import com.lyj.pinstagram.view.main.dialogs.sign.fragments.signup.SignUpFragment

class SignDialogViewModel(
    override val context: AppCompatActivity,
) : DialogViewModel,
    SizeMeasurable {
    val currentViewType: MutableLiveData<SignViewType> = MutableLiveData(SignViewType.SIGN_IN)

    val changeViewTypeCallBack: ChangeViewTypeCallBack = {
        currentViewType.postValue(it)
    }
}


enum class SignViewType(
) {
    SIGN_IN {
        override fun getFragments(changeViewTypeCallBack: ChangeViewTypeCallBack): Fragment =
            SignInFragment.getInstance(changeViewTypeCallBack)

    },
    SIGN_UP {
        override fun getFragments(changeViewTypeCallBack: ChangeViewTypeCallBack): Fragment =
            SignUpFragment.getInstance(changeViewTypeCallBack)

    };

    abstract fun getFragments(changeViewTypeCallBack: ChangeViewTypeCallBack): Fragment
}