package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TourBookingDetailsResponse (
    @SerializedName("Data")
    val Data: ArrayList<TourBookingDetailsModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TourBookingDetailsModel (

    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("TourName")
    val TourName: String?,
    @SerializedName("Overview")
    val Overview: String?,
    @SerializedName("OverallInclusions")
    val OverallInclusions: String?,
    @SerializedName("NoOfDays")
    val NoOfDays: Int?,
    @SerializedName("NoOfNights")
    val NoOfNights: Int?,
    @SerializedName("RateType")
    val RateType: String?,
    @SerializedName("Highlights")
    val Highlights: String?,
    @SerializedName("RoomTypeID")
    val RoomTypeID: Int?,
    @SerializedName("RoomType")
    val RoomType: String?,
    @SerializedName("Inclusions")
    val Inclusions: String?,
    @SerializedName("Exclusions")
    val Exclusions: String?,
    @SerializedName("TourDate")
    val TourDate: String? = null,
    @SerializedName("TotalCost")
    val TotalCost: String? = null,

    @SerializedName("images")
    val TourImages: ArrayList<TourImages>? = null,
    @SerializedName("touritinerary")
    val touritinerary: ArrayList<ItineraryData>? = null,
    @SerializedName("tourHotels")
    val tourHotels: ArrayList<HotelData>? = null,
    @SerializedName("tourhotelcost")
    val tourhotelcost: ArrayList<HotelCostData>? = null,
    @SerializedName("tourfacilities")
    val toufacility: ArrayList<Facility>? = null,
    @SerializedName("touCities")
    val touCities: ArrayList<CityData>? = null,
    @SerializedName("airlineVoucher")
    val airlineVoucher: ArrayList<FlightVoucherModel>? = null,
    @SerializedName("hotelVoucher")
    val hotelVoucher: ArrayList<UpcomingHotelModel>? = null,
    @SerializedName("routeVoucher")
    val routeVoucher: ArrayList<RouteVoucherModel>? = null
)

