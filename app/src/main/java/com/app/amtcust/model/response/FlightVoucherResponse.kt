package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class FlightVoucherResponse (
    @SerializedName("Data")
    val Data: ArrayList<FlightVoucherModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class FlightVoucherModel (
    @SerializedName("SegmentID")
    val ID: Int?,
    @SerializedName("AirlineID")
    val AirlineID: Int?,
    @SerializedName("FlightNo")
    val FlightNo: String?,
    @SerializedName("AirlineName")
    val AirlineName: String?,
    @SerializedName("AirlineLogo")
    val AirlineLogo: String?,
    @SerializedName("PNRNo")
    val PNRNo: String?,
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
    @SerializedName("AirlineVoucherID")
    val AirlineVoucherID: Int?,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String?,

    // Created From App AI005
    @SerializedName("IsCreatedFromApp")
    val IsCreatedFromApp: Boolean?,
    @SerializedName("TicketPurchasedDate")
    val TicketPurchasedDate: String?,
    @SerializedName("TotalPrice")
    val TotalPrice: String?,
    @SerializedName("AirlinevoucherNo")
    val AirlinevoucherNo: String?,
    @SerializedName("AirlineVoucherTicket")
    val AirlineVoucherTicket: String?,
    @SerializedName("DeparturePNRNo")
    val DeparturePNRNo: String?,
    @SerializedName("ArrivalPNRNo")
    val ArrivalPNRNo: String?,
    @SerializedName("NoOfPax")
    val NoOfPax: String?

 )
