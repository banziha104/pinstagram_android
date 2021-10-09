package com.lyj.data.source.remote.entity.geo.reverse

import com.google.gson.annotations.SerializedName
import com.lyj.domain.model.network.geo.ReversedGeoModel
import java.lang.NullPointerException

data class ReversedGeoResponse(
    @field:SerializedName("plus_code")
    val plusCode: PlusCode? = null,

    @field:SerializedName("results")
    val results: List<ResultsItem?>? = null
)

data class AddressComponentsItem(

    @field:SerializedName("types")
    val types: List<String?>? = null,

    @field:SerializedName("short_name")
    val shortName: String? = null,

    @field:SerializedName("long_name")
    val longName: String? = null
) : ReversedGeoModel {
    override val address: String?
        get() = longName
}

data class ResultsItem(

    @field:SerializedName("formatted_address")
    val formattedAddress: String? = null,

    @field:SerializedName("address_components")
    val addressComponents: List<AddressComponentsItem?>? = null
)

data class PlusCode(

    @field:SerializedName("compound_code")
    val compoundCode: String? = null,

    @field:SerializedName("global_code")
    val globalCode: String? = null
)
