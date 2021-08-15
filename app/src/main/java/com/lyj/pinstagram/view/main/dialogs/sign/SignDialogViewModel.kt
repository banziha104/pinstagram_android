package com.lyj.pinstagram.view.main.dialogs.sign

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lyj.core.base.DialogViewModel
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.pinstagram.view.main.dialogs.sign.fragments.signin.SignInFragment
import com.lyj.pinstagram.view.main.dialogs.sign.fragments.signup.SignUpFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignDialogViewModel @Inject constructor (application: Application) : AndroidViewModel(application),
    SizeMeasurable {

    override val context : Context by lazy { getApplication() }

    val currentViewType: MutableLiveData<SignViewType> = MutableLiveData(SignViewType.SIGN_IN)

    val changeViewTypeCallBack: ChangeViewTypeCallBack = {
        currentViewType.postValue(it)
    }
}


enum class SignViewType(
) {
    SIGN_IN {
        override fun getFragments(
            changeViewTypeCallBack: ChangeViewTypeCallBack,
            dismiss: () -> Unit
        ): Fragment =
            SignInFragment.getInstance(changeViewTypeCallBack, dismiss)

    },
    SIGN_UP {
        override fun getFragments(
            changeViewTypeCallBack: ChangeViewTypeCallBack,
            dismiss: () -> Unit
        ): Fragment =
            SignUpFragment.getInstance(dismiss)

    };

    abstract fun getFragments(
        changeViewTypeCallBack: ChangeViewTypeCallBack,
        dismiss: () -> Unit
    ): Fragment
}