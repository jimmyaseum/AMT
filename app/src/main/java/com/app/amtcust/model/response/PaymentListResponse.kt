package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class PaymentListResponse (
    @SerializedName("Data")
    val Data: ArrayList<PaymentReceiptListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
        )

data class PaymentReceiptListModel (
    @SerializedName("ID")
    val ID: Int?,
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
    val Amount: Double?,
    @SerializedName("PaymentFor")
    val PaymentFor: String?,
    @SerializedName("BookBy")
    val BookBy: String?,
    @SerializedName("ApprovedByName")
    val ApprovedByName: String?,
    @SerializedName("PaymentType")
    val PaymentType: String?,
    @SerializedName("ReceiptImage")
    val ReceiptImage: String?,
    @SerializedName("IsCreatedFromApp")
    val IsCreatedFromApp: Boolean? = false
)
