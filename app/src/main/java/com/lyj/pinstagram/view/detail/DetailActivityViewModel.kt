package com.lyj.pinstagram.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyj.api.network.contents.ContentsService
import com.lyj.domain.base.ApiResponse
import com.lyj.domain.network.contents.response.ContentsRetrieveResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailActivityViewModel @Inject constructor(
    private val contentsService: ContentsService
) : ViewModel() {
    fun requestContents(id: Long, block: (ApiResponse<ContentsRetrieveResponse>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = contentsService.getById(id)
            block(response)
        }
    }

}