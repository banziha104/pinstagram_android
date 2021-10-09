package com.lyj.pinstagram.view.main.fragments.home.adapter

import com.lyj.domain.model.network.contents.ContentsModel

data class HomeGridItem (
    val imageUrl : String,
    val id : Long
){
    companion object{
        fun fromResponse(response : ContentsModel) : HomeGridItem = HomeGridItem(response.picture,response.contentsId)
    }
}