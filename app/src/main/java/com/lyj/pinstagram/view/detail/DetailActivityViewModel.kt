package com.lyj.pinstagram.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyj.data.source.remote.http.contents.ContentsService
import com.lyj.data.source.remote.entity.ApiResponse
import com.lyj.data.source.remote.entity.contents.response.ContentsRetrieveResponse
import com.lyj.domain.model.network.ApiModel
import com.lyj.domain.model.network.contents.ContentsModel
import com.lyj.domain.usecase.network.contents.RequestContentsByIdUseCaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailActivityViewModel @Inject constructor(
    private val requestContentsByIdUseCaseUseCase: RequestContentsByIdUseCaseUseCase
) : ViewModel() {
    fun requestContents(id: Long, block: (ApiModel<ContentsModel>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = requestContentsByIdUseCaseUseCase.execute(id)
            block(response)
        }
    }
}