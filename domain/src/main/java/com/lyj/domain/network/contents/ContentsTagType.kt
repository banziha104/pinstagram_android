package com.lyj.domain.network.contents

import android.content.Context
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializer
import com.google.gson.annotations.SerializedName
import com.lyj.domain.R
import java.lang.reflect.Type

enum class ContentsTagType(
    val origin : Int,
    val kor : Int
) {
    @SerializedName("FOOD")
    FOOD(R.string.food_origin,R.string.food_kor),
    @SerializedName("SHOP")
    SHOP(R.string.shop_origin,R.string.shop_kor),
    @SerializedName("PLACE")
    PLACE(R.string.place_origin,R.string.place_kor),
    @SerializedName("SERVICE")
    SERVICE(R.string.service_origin,R.string.service_kor),
    @SerializedName("ETC")
    ETC(R.string.etc_origin,R.string.etc_kor);

    companion object{
        private var stringResourceMap : MutableMap<String, Int>? = null
        fun findByKoreanTitle(context: Context, kor: String) : ContentsTagType?{
            if (stringResourceMap == null){
                val map = mutableMapOf<String,Int>()
                values().forEach {
                    map[context.getString(it.kor)] = it.kor
                }
                stringResourceMap = map
            }
            return values().firstOrNull { it.kor == stringResourceMap?.get(kor) }
        }
    }
}
