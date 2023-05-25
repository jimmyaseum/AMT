package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TourPaxInformationResponse(
    @SerializedName("Data")
    val Data: ArrayList<TourPaxInformationModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TourPaxInformationModel(
    @SerializedName("RoomNo")
    val RoomNo: String?,
    @SerializedName("paxData")
    val paxData: ArrayList<paxDataModel>? = null
)
data class paxDataModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("RoomNo")
    val RoomNo: String?,
    @SerializedName("Initial")
    val Initial: String?,
    @SerializedName("FirstName")
    val FirstName: String?,
    @SerializedName("LastName")
    val LastName: String?,
    @SerializedName("MobileNo")
    val MobileNo: String?,
    @SerializedName("Gender")
    val Gender: String?,
    @SerializedName("DOB")
    val DOB: String?,
    @SerializedName("Age")
    val Age: String?,
    @SerializedName("PassportNo")
    val PassportNo: String?
)

