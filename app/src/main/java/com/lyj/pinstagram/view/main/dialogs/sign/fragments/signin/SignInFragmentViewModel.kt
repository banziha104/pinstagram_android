package com.lyj.pinstagram.view.main.dialogs.sign.fragments.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lyj.data.source.local.LocalDatabase
import com.lyj.data.source.remote.http.auth.AuthenticationService
import com.lyj.core.extension.android.resString
import com.lyj.customui.dialog.edittext.ValidateRule
import com.lyj.data.source.local.entity.TOKEN_ID
import com.lyj.data.source.local.entity.TokenEntity
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.auth.request.SignInRequest
import com.lyj.domain.network.auth.response.SignInResponse
import com.lyj.pinstagram.R
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SignInFragmentViewModel @Inject constructor(
    application: Application,
    private val authenticationService: AuthenticationService,
    private val database: LocalDatabase
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

    fun requestSignIn(request: SignInRequest): Single<ApiResponse<SignInResponse>> =
        authenticationService.signIn(request).observeOn(Schedulers.io())

    fun saveToken(token : String) : Completable = database.tokenDao().insert(
        TokenEntity(
            TOKEN_ID,token)
    ).subscribeOn(Schedulers.io())
}