package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class UpcomingHotelResponse (
    @SerializedName("Data")
    val Data: ArrayList<UpcomingHotelModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
        )
data class UpcomingHotelModel (

    @SerializedName("HotelVoucherID")
    val HotelVoucherID: Int?,
    @SerializedName("TourID")
    val TourID: Int?,
    @SerializedName("TourBookingNo")
    val TourBookingNo: String?,
    @SerializedName("TourName")
    val TourName: String?,
    @SerializedName("HotelID")
    val HotelID: Int?,
    @SerializedName("HotelName")
    val HotelName: String?,
    @SerializedName("HotelAddress")
    val HotelAddress: String?,
    @SerializedName("HotelVoucher")
    val HotelVoucher: String?,
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
    @SerializedName("CheckoutDate")
    val CheckoutDate: String?,
    @SerializedName("HotelImage")
    val HotelImage: String?,

    // Created From App AI005
    @SerializedName("IsCreatedFromApp")
    val IsCreatedFromApp: Boolean?,
    @SerializedName("HotelVoucherImage")
    val HotelVoucherImage: String?

)


