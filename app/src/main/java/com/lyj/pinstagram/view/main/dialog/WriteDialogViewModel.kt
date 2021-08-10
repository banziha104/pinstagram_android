package com.lyj.pinstagram.view.main.dialog

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.lyj.core.base.DialogViewModel
import com.lyj.core.module.size.SizeMeasurable
import com.lyj.customui.dialog.edittext.ValidateRule
import com.lyj.pinstagram.R
import com.lyj.pinstagram.location.LocationEventManager
import com.lyj.pinstagram.location.protocol.OnceLocationGetter
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import javax.inject.Inject

class WriteDialogViewModel(
    override val context: FragmentActivity,
    locationProvider: LocationEventManager
) : DialogViewModel,
    SizeMeasurable,
    OnceLocationGetter by locationProvider{
    val titleRule : List<ValidateRule> = listOf(
        ValidateRule(getString(R.string.write_validate_title_empty)){ it.isNotBlank() },
        ValidateRule(getString(R.string.write_validate_title_length)){ it.length >= 4 }
    )

    val descriptionRule : List<ValidateRule> = listOf(
        ValidateRule(getString(R.string.write_validate_description_empty)){ it.isNotBlank() },
        ValidateRule(getString(R.string.write_validate_description_length)){ it.length >= 4 }
    )
}