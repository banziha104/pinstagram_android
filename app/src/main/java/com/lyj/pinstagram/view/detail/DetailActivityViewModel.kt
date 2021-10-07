package com.lyj.pinstagram.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyj.data.source.remote.http.contents.ContentsService
import com.lyj.data.source.local.temp.base.ApiResponse
import com.lyj.data.source.local.temp.network.contents.response.ContentsRetrieveResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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