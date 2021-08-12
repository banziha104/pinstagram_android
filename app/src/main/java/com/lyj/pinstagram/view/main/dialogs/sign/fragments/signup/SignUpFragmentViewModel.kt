package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signup

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.lyj.api.database.LocalDatabase
import com.lyj.api.network.auth.AuthenticationService
import com.lyj.customui.dialog.edittext.ValidateRule
import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.auth.request.SignUpRequest
import com.lyj.domain.network.auth.response.SignUpResponse
import com.lyj.pinstagram.R
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SignUpFragmentViewModel @Inject constructor(
    application: Application,
    private val authenticationService: AuthenticationService,
    private val database: LocalDatabase
) : AndroidViewModel(application) {
    private val currentContext: Context by lazy { getApplication<Application>().applicationContext }

    private fun getString(@StringRes id: Int) = currentContext.getString(id)

    val emailRule: List<ValidateRule> = listOf(
        ValidateRule(getString(R.string.validate_message_empty)) { it.isNotBlank() },
        ValidateRule(getString(R.string.validate_message_length)) { it.length >= 4 },
        ValidateRule(getString(R.string.validate_message_email)) { '@' in it }
    )

    val passWordRule: List<ValidateRule> = listOf(
        ValidateRule(getString(R.string.validate_message_empty)) { it.isNotBlank() },
        ValidateRule(getString(R.string.validate_message_length)) { it.length >= 4 }
    )

    val nameRule: List<ValidateRule> = listOf(
        ValidateRule(getString(R.string.validate_message_empty)) { it.isNotBlank() },
        ValidateRule(getString(R.string.validate_message_length)) { it.length >= 4 }
    )

    fun requestSignUp(request: SignUpRequest): Single<ApiResponse<SignUpResponse>> =
        authenticationService
            .signUp(request)
            .subscribeOn(Schedulers.io())
}