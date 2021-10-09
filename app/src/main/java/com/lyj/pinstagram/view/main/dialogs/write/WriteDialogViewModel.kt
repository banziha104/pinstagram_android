package com.lyj.pinstagram.view.main.dialogs.write

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Location
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.lyj.data.source.local.LocalDatabase
import com.lyj.data.source.remote.http.contents.ContentsService
import com.lyj.core.extension.android.resString
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.customui.dialog.edittext.ErrorMessage
import com.lyj.customui.dialog.edittext.IsValidated
import com.lyj.customui.dialog.edittext.ValidateRule
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.local.entity.TokenEntity
import com.lyj.data.source.remote.entity.contents.request.ContentsCreateRequest
import com.lyj.domain.model.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.domain.usecase.android.location.GetLocationUseCase
import com.lyj.domain.usecase.local.token.FindTokenUseCase
import com.lyj.domain.usecase.network.contents.RequestCreateContentsUseCase
import com.lyj.domain.usecase.network.storage.RequestUploadStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class WriteDialogViewModel @Inject constructor(
    application: Application,
    val getLocationUseCase: GetLocationUseCase,
    val createContentsUseCase: RequestCreateContentsUseCase,
    val findTokenUseCase: FindTokenUseCase,
    val uploadStorageUseCase: RequestUploadStorageUseCase,
) : AndroidViewModel(application),
    SizeMeasurable{

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
}

data class WriteRequestAsset(
    val title: Pair<IsValidated, ErrorMessage?>,
    val description: Pair<IsValidated, ErrorMessage?>,
    val tag: ContentsTagType,
    val uri: Uri
)