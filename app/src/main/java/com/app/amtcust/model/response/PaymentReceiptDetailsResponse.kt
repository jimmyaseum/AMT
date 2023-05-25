package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class PaymentReceiptDetailsResponse (
    @SerializedName("Data")
    val Data: PaymentReceiptDetailsModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class PaymentReceiptDetailsModel (
    @SerializedName("BookingNo")
    val BookingNo: String?,
    @SerializedName("TourName")
    val TourName: String?,
    @SerializedName("ReceiptNo")
    val ReceiptNo: String?,
    @SerializedName("Name")
    val Name: String?,
    @SerializedName("ReceiptStatus")
    val ReceiptStatus: String?,
    @SerializedName("PaymentDate")
    val PaymentDate: String?,
    @SerializedName("Amount")
    val Amount: String?,
    @SerializedName("PaymentType")
    val PaymentType: String?,
    @SerializedName("PaymentFor")
    val PaymentFor: String?,
    @SerializedName("BookBy")
    val BookBy: String?,
    @SerializedName("BranchName")
    val BranchName: String?,
    @SerializedName("Address")
    val Address: String?,
    @SerializedName("TotalNoOfPax")
    val TotalNoOfPax: String?,
    @SerializedName("FullTicket")
    val FullTicket: String?,
    @SerializedName("TotalCWB")
    val TotalCWB: String?,
    @SerializedName("TotalCNB")
    val TotalCNB: String?,
    @SerializedName("TotalNoOfRooms")
    val TotalNoOfRooms: String?,
    @SerializedName("TotalNoOfSeats")
    val TotalNoOfSeats: String?,
    @SerializedName("TourDateCode")
    val TourDateCode: String?,
    @SerializedName("StartEndDate")
    val StartEndDate: String?,
    @SerializedName("ExtraBed")
    val ExtraBed: String?,
    @SerializedName("VehicleType")
    val VehicleType: String?,
    @SerializedName("PaymentLink")
    val PaymentLink: String?,
    @SerializedName("TourbookingLink")
    val TourbookingLink: String?
)




