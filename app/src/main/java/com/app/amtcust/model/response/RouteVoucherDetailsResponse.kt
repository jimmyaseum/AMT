package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class RouteVoucherDetailsResponse(
    @SerializedName("Data")
    val Data: ArrayList<RouteVoucherDetailsModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class RouteVoucherDetailsModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String?,
    @SerializedName("GuestName")
    val GuestName: String?,
    @SerializedName("VoucherDate")
    val VoucherDate: String?,
    @SerializedName("StartDate")
    val StartDate: String?,
    @SerializedName("EndDate")
    val EndDate: String?,
    @SerializedName("TransporterID")
    val TransporterID: Int?,
    @SerializedName("TransporterName")
    val TransporterName: String?,
    @SerializedName("TransporterMobileNo1")
    val TransporterMobileNo1: String?,
    @SerializedName("TransporterMobileNo2")
    val TransporterMobileNo2: String?,
    @SerializedName("VehicleType")
    val VehicleType: String?,
    @SerializedName("ApprovedBy")
    val ApprovedBy: String?,
    @SerializedName("route")
    val route: ArrayList<RouteDetailsModel>?
        )
data class RouteDetailsModel (
    @SerializedName("DayNo")
    val DayNo: Int?,
    @SerializedName("Date")
    val Date: String?,
    @SerializedName("CityID")
    val CityID: Int?,
    @SerializedName("CityName")
    val CityName: String?,
    @SerializedName("HotelID")
    val HotelID: Int?,
    @SerializedName("HotelName")
    val HotelName: String?,
    @SerializedName("RouteName")
    val RouteName: String?,
    @SerializedName("Description")
    val Description: String?,
    @SerializedName("RouteID")
    val RouteID: Int?
        )
