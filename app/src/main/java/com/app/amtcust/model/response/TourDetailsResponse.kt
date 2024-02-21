package com.app.amtcust.model.response

import com.app.amtcust.model.AttachmentModel
import com.google.gson.annotations.SerializedName

data class TourDetailsResponse(
    @SerializedName("Data")
    val Data: ArrayList<TourDetailsModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TourDetailsModel (
    @SerializedName("TourName")
    val TourName: String?,
    @SerializedName("NoOfDays")
    val NoOfDays: Int?,
    @SerializedName("NoOfNights")
    val NoOfNights: Int?,
    @SerializedName("OverallInclusions")
    val OverallInclusions: String?,
    @SerializedName("Overview")
    val Overview: String?,
    @SerializedName("Highlights")
    val Highlights: String?,
    @SerializedName("RateType")
    val RateType: String?,
    @SerializedName("touCities")
    val touCities: ArrayList<CityData>? = null,
    @SerializedName("tourDocuments")
    var tourDocuments: ArrayList<AttachmentModel> = arrayListOf(),
    @SerializedName("tourfacilities")
    val toufacility: ArrayList<Facility>? = null,
    @SerializedName("images")
    val TourImages: ArrayList<TourImages>? = null,
    @SerializedName("tourdates")
    val tourdates: ArrayList<MonthDataModel>? = null,
//    @SerializedName("toudate")
//    val Toudate: ArrayList<TourDates>? = null,
    @SerializedName("tourcost")
    val tourcost: ArrayList<HotelRoom>? = null,
    @SerializedName("touritinerary")
    val touritinerary: ArrayList<ItineraryData>? = null,
//    @SerializedName("tourHotels")
//    val tourHotels: ArrayList<HotelData>? = null,
    @SerializedName("tourhotelcost")
    val tourhotelcost: ArrayList<HotelCostData>? = null,
    @SerializedName("Inclusions")
    val Inclusions: String?,
    @SerializedName("Exclusions")
    val Exclusions: String?,
    @SerializedName("TourCities")
    val TourCities: String?,

    @SerializedName("PdfPath")
    val PdfPath: String? = null



)

data class TourImages (
    @SerializedName("TourID")
    val TourID: Int?,
    @SerializedName("TourImage")
    val TourImage: String?
)
data class TourDates (
    @SerializedName("TourDate")
    val TourDate: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)
data class ItineraryData (
    @SerializedName("ItineraryTitle")
    val ItineraryTitle: String?,
    @SerializedName("ItineraryDescription")
    val ItineraryDescription: String?,
    @SerializedName("ItineraryDay")
    val ItineraryDay: Int?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("tourfcs")
    val tourfcs: ArrayList<Facility>? = null
)

data class HotelData (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("Days")
    val Days: Int?,
    @SerializedName("HotelName")
    val HotelName: String?,
    @SerializedName("HotelImage")
    val HotelImage: String?,
    @SerializedName("HotelAddress")
    val HotelAddress: String?,
    @SerializedName("StarRating")
    val StarRating: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)
data class HotelCostData (
    @SerializedName("RoomType")
    val RoomType: String?,
    @SerializedName("TwinSharingRate")
    val TwinSharingRate: String?,
    @SerializedName("ThreeSharingRate")
    val ThreeSharingRate: String?,
    @SerializedName("FourSharingRate")
    val FourSharingRate: String?,
    @SerializedName("CWBRate")
    val CWBRate: String?,
    @SerializedName("CNBRate")
    val CNBRate: String?,
    @SerializedName("ExtraAdultRate")
    val ExtraAdultRate: String?,
    @SerializedName("InfantRate")
    val InfantRate: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)
data class CityData (
    @SerializedName("CityID")
    val CityID: Int?,
    @SerializedName("CityName")
    val CityName: String?,
    @SerializedName("NoOfNights")
    val NoOfNights: Int?
)

