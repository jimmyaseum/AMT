package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class RouteVoucherResponse(

    @SerializedName("Data")
    val Data: ArrayList<RouteVoucherModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int

)
data class RouteVoucherModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String?,
    @SerializedName("VehicleType")
    val VehicleType: String?,
    @SerializedName("VehicleImage")
    val VehicleImage: String?,
    @SerializedName("StartDate")
    val StartDate: String?,
    @SerializedName("EndDate")
    val EndDate: String?,
    @SerializedName("NoOfPax")
    val NoOfPax: Int?
    )

