package com.lyj.pinstagram.view.main.fragments.map

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.lyj.domain.usecase.android.location.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor() : ViewModel()