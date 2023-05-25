package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TourBookingResponse(
    @SerializedName("Data")
    val Data: TourBooking? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int

)
data class TourBooking (
    @SerializedName("ongoingTrips")
    val ongoingTrips: ArrayList<TourBookingModel>? = null,
    @SerializedName("completedTrip")
    val completedTrip: ArrayList<TourBookingModel>? = null,
    @SerializedName("upcomingTrips")
    val upcomingTrips: ArrayList<TourBookingModel>? = null
    )


data class TourBookingModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("TourID")
    val TourID: Int?,
    @SerializedName("TourName")
    val TourName: String?,
    @SerializedName("PBN")
    val PBN: String?,
    @SerializedName("TotalCost")
    val TotalCost: String?,
    @SerializedName("TourImage")
    val TourImage: String?,
    @SerializedName("TourStartDate")
    val TourStartDate: String?,
    @SerializedName("TourEndDate")
    val TourEndDate: String?,
    @SerializedName("customers")
    val customers: ArrayList<FamilyMember>?
)
data class FamilyMember (
    @SerializedName("CustomerImage")
    val CustomerImage: String?,
    @SerializedName("FirstName")
    val FirstName: String?,
    @SerializedName("LastName")
    val LastName: String?
    )