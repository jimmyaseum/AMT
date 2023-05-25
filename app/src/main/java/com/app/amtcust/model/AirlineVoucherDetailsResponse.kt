package com.app.amtcust.model

import com.google.gson.annotations.SerializedName

data class AirlineVoucherDetailsResponse (
    @SerializedName("Data")
    val Data: AirlineVoucherDetailsModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class AirlineVoucherDetailsModel (
    @SerializedName("TourBookingNo")
    val TourBookingNo: String?,
    @SerializedName("NoOfPax")
    val NoOfPax: String?,
    @SerializedName("AirlineLogo")
    val AirlineLogo: String?,
    @SerializedName("AirlineName")
    val AirlineName: String?,
    @SerializedName("FromAirport")
    val FromAirport: String?,
    @SerializedName("FromAirportCode")
    val FromAirportCode: String?,
    @SerializedName("ToAirport")
    val ToAirport: String?,
    @SerializedName("ToAirportCode")
    val ToAirportCode: String?,
    @SerializedName("DepartureDate")
    val DepartureDate: String?,
    @SerializedName("DepartureTime")
    val DepartureTime: String?,
    @SerializedName("ArrivalDate")
    val ArrivalDate: String?,
    @SerializedName("ArrivalTime")
    val ArrivalTime: String?,
    @SerializedName("PNRNo")
    val PNRNo: String?,
    @SerializedName("JourneyType")
    val JourneyType: String?,
    @SerializedName("passengers")
    val passengers: ArrayList<Passenger>? = null,
    @SerializedName("AirlineVoucherTicket")
    val AirlineVoucherTicket: String?
 )
data class Passenger (
    @SerializedName("CustomerName")
    val CustomerName: String?,
    @SerializedName("FlightNo")
    val FlightNo: String?,
    @SerializedName("AirlineClassID")
    val AirlineClassID: String?,
    @SerializedName("Class")
    val Class: String?
    )



