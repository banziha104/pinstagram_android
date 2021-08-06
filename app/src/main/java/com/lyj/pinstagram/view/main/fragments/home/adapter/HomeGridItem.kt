package com.lyj.pinstagram.view.main.fragments.home.adapter

import com.lyj.domain.network.contents.response.ContentsRetrieveResponse

data class HomeGridItem (
    val imageUrl : String,
    val id : Long
){
    companion object{
        fun fromResponse(response : ContentsRetrieveResponse) : HomeGridItem = HomeGridItem(response.picture,response.contentsId)
    }

}