package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lyj.core.extension.android.resString
import com.lyj.customui.dialog.edittext.ValidateRule
import com.lyj.domain.usecase.local.token.InsertTokenUseCase
import com.lyj.domain.usecase.network.auth.sign.RequestSignInUseCase
import com.lyj.pinstagram.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInFragmentViewModel @Inject constructor(
    application: Application,
    val requestSignInUseCase: RequestSignInUseCase,
    val insertTokenUseCase: InsertTokenUseCase,
) : AndroidViewModel(application) {

    val emailRule: List<ValidateRule> = listOf(
        ValidateRule(resString(R.string.validate_message_empty)) { it.isNotBlank() },
        ValidateRule(resString(R.string.validate_message_length)) { it.length >= 4 },
        ValidateRule(resString(R.string.validate_message_email)) { '@' in it }
    )

    val passwordRule: List<ValidateRule> = listOf(
        ValidateRule(resString(R.string.validate_message_empty)) { it.isNotBlank() },
        ValidateRule(resString(R.string.validate_message_length)) { it.length >= 4 }
    )
}