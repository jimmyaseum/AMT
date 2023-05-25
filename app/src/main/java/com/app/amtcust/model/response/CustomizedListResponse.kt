package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class CustomizedListResponse(
    @SerializedName("Data")
    val Data: ArrayList<CustomizedListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class CustomizedListModel(
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