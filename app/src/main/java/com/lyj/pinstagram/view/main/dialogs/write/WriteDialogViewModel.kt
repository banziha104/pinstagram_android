package com.lyj.pinstagram.view.main.dialogs.write

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.lyj.api.database.LocalDatabase
import com.lyj.api.network.contents.ContentsService
import com.lyj.api.storage.StorageUploader
import com.lyj.core.extension.android.resString
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.customui.dialog.edittext.ErrorMessage
import com.lyj.customui.dialog.edittext.IsValidated
import com.lyj.customui.dialog.edittext.ValidateRule
import com.lyj.domain.base.ApiResponse
import com.lyj.domain.localdb.TokenEntity
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.domain.network.contents.request.ContentsCreateRequest
import com.lyj.pinstagram.R
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.location.protocol.OnceLocationGetter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WriteDialogViewModel @Inject constructor(
    application: Application,
    private val contentsService: ContentsService,
    locationProvider: LocationEventManager,
    private val localDatabase: LocalDatabase,
    private val storageUploader: StorageUploader,
) : AndroidViewModel(application),
    SizeMeasurable,
    OnceLocationGetter by locationProvider {

    override val context: Context by lazy { getApplication() }

    val titleRule: List<ValidateRule> = listOf(
        ValidateRule(resString(R.string.validate_message_empty)) { it.isNotBlank() },
        ValidateRule(resString(R.string.validate_message_length)) { it.length >= 4 }
    )

    val descriptionRule: List<ValidateRule> = listOf(
        ValidateRule(resString(R.string.validate_message_empty)) { it.isNotBlank() },
        ValidateRule(resString(R.string.validate_message_length)) { it.length >= 4 }
    )

    val spinnerItems = ContentsTagType.values()

    var currentAsset: WriteRequestAsset? = null


    fun requestUpload(uri: Uri): Single<String> = storageUploader.upload(uri)

    fun requestCreateContents(token: String, createRequest: ContentsCreateRequest): Single<ApiResponse<Long>> =
        contentsService.createContents(token, createRequest).subscribeOn(Schedulers.io())

    fun getToken(): Single<List<TokenEntity>> =
        localDatabase.tokenDao().findToken().subscribeOn(Schedulers.io())
}

data class WriteRequestAsset(
    val title: Pair<IsValidated, ErrorMessage?>,
    val description: Pair<IsValidated, ErrorMessage?>,
    val tag: ContentsTagType,
    val uri: Uri
)