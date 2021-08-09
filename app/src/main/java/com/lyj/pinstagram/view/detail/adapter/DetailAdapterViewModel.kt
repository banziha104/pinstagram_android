package com.lyj.pinstagram.view.detail.adapter

import android.content.Context
import android.media.MediaCasException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.lyj.core.base.AdapterViewModel
import com.lyj.domain.network.contents.response.ContentsRetrieveResponse
import com.lyj.pinstagram.R
import java.lang.RuntimeException
import kotlin.jvm.Throws

class DetailAdapterViewModel(
    val context: Context,
    val data: ContentsRetrieveResponse,
    val lifecycle: Lifecycle
) : AdapterViewModel<DetailItemType> {

    override val items: List<DetailItemType> = DetailItemType.values().toList()

}

enum class DetailItemType(
    val title: Int?,
    val viewType: DetailAdapterViewType,
    val parsedType: Class<out DetailParsedType>
) {
    IMAGE(
        null,
        DetailAdapterViewType.IMAGE,
        DetailParsedType.StringParsedType::class.java
    ),
    TITLE(
        R.string.detail_item_title,
        DetailAdapterViewType.TEXT,
        DetailParsedType.StringParsedType::class.java
    ),
    DESCRIPTION(
        R.string.detail_item_description,
        DetailAdapterViewType.TEXT,
        DetailParsedType.StringParsedType::class.java
    ),
    TAG(
        R.string.detail_item_tag,
        DetailAdapterViewType.TEXT,
        DetailParsedType.IntParsedType::class.java
    ),
    FULL_ADDRESS(
        R.string.detail_item_full_address,
        DetailAdapterViewType.TEXT,
        DetailParsedType.StringParsedType::class.java
    ),
    MAP(
        R.string.detail_item_map,
        DetailAdapterViewType.MAP,
        DetailParsedType.LatLngParsedType::class.java
    );

    @Throws(RuntimeException::class)
    inline fun <reified CASTED_CLASS : DetailParsedType> parseData(
        data: ContentsRetrieveResponse,
    ): CASTED_CLASS {
        if (this.parsedType != CASTED_CLASS::class.java) throw RuntimeException("지원하지 않는 타입")
        return when (this) {
            IMAGE -> DetailParsedType.StringParsedType(data.picture)
            TITLE -> DetailParsedType.StringParsedType(data.title)
            DESCRIPTION -> DetailParsedType.StringParsedType(data.description)
            TAG -> DetailParsedType.IntParsedType(data.tag.kor)
            FULL_ADDRESS -> DetailParsedType.StringParsedType(data.fullAddress)
            MAP -> DetailParsedType.LatLngParsedType(LatLng(data.lat, data.lng))
        } as CASTED_CLASS
    }
}

sealed class DetailParsedType {
    class StringParsedType(val item: String) : DetailParsedType()
    class IntParsedType(val item: Int) : DetailParsedType()
    class LatLngParsedType(val item: LatLng) : DetailParsedType()
}

enum class DetailAdapterViewType(
    val type: Int
) {
    TEXT(1),
    MAP(2),
    IMAGE(3),
}

