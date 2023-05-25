package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class HimalayanListResponse(
    @SerializedName("Data")
    val Data: ArrayList<HimalayanListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class HimalayanListModel(
    @SerializedName("ID")
    val ID: Int? = 0,
    @SerializedName("TourName")
    val TourName: String? = null,
    @SerializedName("TourImage")
    val TourImage: String? = null,
    @SerializedName("FourSharingRate")
    val FourSharingRate: String? = null,
    @SerializedName("SectorID")
    val SectorID: Int? = null,
    @SerializedName("SectorName")
    val SectorName: String? = null
)