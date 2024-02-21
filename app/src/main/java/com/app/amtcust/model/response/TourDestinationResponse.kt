package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TourDestinationResponse(
    @SerializedName("Data")
    val Data: ArrayList<TourDestinationListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TourDestinationListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("TourName")
    val TourName: String?,
    @SerializedName("Cities")
    val Cities: Int? = 0,
    @SerializedName("toucities")
    val toucities: ArrayList<City>? = ArrayList(),
    @SerializedName("hotelroom")
    val hotelroom: ArrayList<HotelRoom>? = ArrayList(),
    @SerializedName("toufacility")
    val toufacility: ArrayList<Facility> = ArrayList(),
    @SerializedName("TotalNoOfTours")
    val TotalNoOfTours: Int?,
    @SerializedName("TourImage")
    val TourImage: String?,
    @SerializedName("NoOfDays")
    val NoOfDays: Int?,
    @SerializedName("NoOfNights")
    val NoOfNights: Int?,
    @SerializedName("Ratetype")
    val Ratetype: String?,
    @SerializedName("RoomTypeID")
    val RoomTypeID: Int?,
    @SerializedName("RoomType")
    val RoomType: String?,
    @SerializedName("TourID")
    val TourID: Int?,
    @SerializedName("TourURL")
    val TourURL: String?,
    @SerializedName("OverallInclusions")
    val OverallInclusions: String?,
    @SerializedName("Rate")
    val Rate: String?,
    @SerializedName("TourCities")
    val TourCities: String?

)

data class City (
    @SerializedName("PlaceID")
    val PlaceID: Int?,
    @SerializedName("PlaceName")
    val PlaceName: String?
)

data class HotelRoom (
    @SerializedName("RoomTypeID")
    val RoomTypeID: Int?,
    @SerializedName("RoomType")
    val RoomType: String?,
    @SerializedName("Rate")
    val Rate: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false

)

data class Facility (
    @SerializedName("Name")
    val Name: String?,
    @SerializedName("Image")
    val Image: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false

)
