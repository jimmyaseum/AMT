package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class OngoingTourDataResponse(
    @SerializedName("Data")
    val Data: OngoingTourDataModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class OngoingTourDataModel (
    //general information
    @SerializedName("ID")
    val ID: Int = 0,
    @SerializedName("TourID")
    val TourID: Int = 0,
    @SerializedName("TourName")
    val TourName: String = "",
    @SerializedName("TourCode")
    val TourDateCode: String = ""
    )
