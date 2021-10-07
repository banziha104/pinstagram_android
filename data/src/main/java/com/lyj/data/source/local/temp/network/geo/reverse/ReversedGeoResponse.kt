package com.lyj.data.source.local.temp.network.geo.reverse

import com.google.gson.annotations.SerializedName

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
)

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
