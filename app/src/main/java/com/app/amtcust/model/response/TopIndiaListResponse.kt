package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TopIndiaListResponse(
    @SerializedName("Data")
    val Data: ArrayList<TopIndiaListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TopIndiaListModel(
    @SerializedName("ID")
    val ID: Int? = 0,
    @SerializedName("SectorName")
    val SectorName: String? = null,
    @SerializedName("DestinationImage")
    val DestinationImage: String? = null

)