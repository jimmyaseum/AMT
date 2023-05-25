package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class HotelVoucherDetailsResponse
    (
        @SerializedName("Data")
        val Data: HotelVoucherDetailsModel? = null,
        @SerializedName("Details")
        val Details: Any?,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
    )
data class HotelVoucherDetailsModel
    (
        @SerializedName("TourID")
        val TourID: Int?,
        @SerializedName("TourName")
        val TourName: String?,
        @SerializedName("HotelID")
        val HotelID: Int?,
        @SerializedName("HotelName")
        val HotelName: String?,
        @SerializedName("HotelAddress")
        val HotelAddress: String?,
        @SerializedName("PBN")
        val PBN: String?,
        @SerializedName("HotelMobileNo")
        val HotelMobileNo: String?,
        @SerializedName("RoomTypeID")
        val RoomTypeID: Int?,
        @SerializedName("RoomType")
        val RoomType: String?,
        @SerializedName("TotalNoOfRooms")
        val TotalNoOfRooms: Int?,
        @SerializedName("TotalNoOfPax")
        val TotalNoOfPax: Int?,
        @SerializedName("CheckinDate")
        val CheckinDate: String?,
        @SerializedName("MealPlanID")
        val MealPlanID: Int?,
        @SerializedName("MealPlan")
        val MealPlan: String?,
        @SerializedName("CheckoutDate")
        val CheckoutDate: String?,
        @SerializedName("BookBy")
        val BookBy: String?,
        @SerializedName("ApprovedBy")
        val ApprovedBy: String?,
        @SerializedName("Supplier")
        val Supplier: String?,
        @SerializedName("NoOfNights")
        val NoOfNights: Int?,
        @SerializedName("Manager")
        val Manager: String?,
        @SerializedName("ManagerMobileNo")
        val ManagerMobileNo: String?,
        @SerializedName("ManagerEmail")
        val ManagerEmail: String?,
        @SerializedName("guestDetails")
        val guestDetails: ArrayList<GuestDetailsModel>?,
        @SerializedName("images")
        val images: ArrayList<HotelImages>?
    )

data class HotelImages (
    @SerializedName("HotelImage")
    val HotelImage: String?
        )
data class GuestDetailsModel
    (
        @SerializedName("Name")
        val Name: String?,
        @SerializedName("MobileNo")
        val MobileNo: String?
    )

