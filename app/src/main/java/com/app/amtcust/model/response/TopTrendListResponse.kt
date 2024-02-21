package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TopTrendListResponse(
    @SerializedName("Data")
    val Data: ArrayList<TopTrendListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TopTrendListModel(
    @SerializedName("ID")
    val ID: Int? = null,
    @SerializedName("SectorName")
    val SectorName: String? = null,
    @SerializedName("DestinationImage")
    val DestinationImage: String? = null,
    @SerializedName("DestinationURL")
    val DestinationURL: String? = null


)