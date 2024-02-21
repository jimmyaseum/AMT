package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class SectorListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("RegionID")
    val RegionID: Int?,
    @SerializedName("RegionName")
    val RegionName: String?,
    @SerializedName("SectorName")
    val SectorName: String?,
    @SerializedName("DestinationURL")
    val DestinationURL: String? = null,
    @SerializedName("SectorType")
    val SectorType: String?,
    @SerializedName("Prefix")
    val Prefix: String?,
    @SerializedName("DestinationImage")
    val DestinationImage: String?
)

