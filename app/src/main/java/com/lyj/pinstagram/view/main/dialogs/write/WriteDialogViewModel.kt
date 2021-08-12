package com.lyj.pinstagram.view.main.dialogs.write

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.lyj.api.network.contents.ContentsService
import com.lyj.api.storage.StorageUploader
import com.lyj.core.base.DialogViewModel
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.core.rx.AutoActivatedDisposable
import com.lyj.customui.dialog.edittext.ErrorMessage
import com.lyj.customui.dialog.edittext.IsValidated
import com.lyj.customui.dialog.edittext.ValidateRule
import com.lyj.domain.network.contents.ContentsTagType
import com.lyj.pinstagram.R
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.location.protocol.OnceLocationGetter
import io.reactivex.rxjava3.core.Single

class WriteDialogViewModel(
    override val context: FragmentActivity,
    val contentsService: ContentsService,
    val viewDisposable: AutoActivatedDisposable,
    locationProvider: LocationEventManager,
    val storageUploader: StorageUploader,
) : DialogViewModel,
    SizeMeasurable,
    OnceLocationGetter by locationProvider{

    val titleRule : List<ValidateRule> = listOf(
        ValidateRule(getString(R.string.validate_message_empty)){ it.isNotBlank() },
        ValidateRule(getString(R.string.validate_message_length)){ it.length >= 4 }
    )

    val descriptionRule : List<ValidateRule> = listOf(
        ValidateRule(getString(R.string.validate_message_empty)){ it.isNotBlank() },
        ValidateRule(getString(R.string.validate_message_length)){ it.length >= 4 }
    )

    val spinnerItems = ContentsTagType.values()

    var currentAsset : WriteRequestAsset? = null


    fun requestUpload(uri : Uri) : Single<String> = storageUploader.upload(uri)

//    fun requestCreateContents(contentsCreateRequest: ContentsCreateRequest) = contentsService.createContents()
}

data class WriteRequestAsset(
    val title : Pair<IsValidated,ErrorMessage?>,
    val description : Pair<IsValidated,ErrorMessage?>,
    val tag : ContentsTagType,
    val uri : Uri
)