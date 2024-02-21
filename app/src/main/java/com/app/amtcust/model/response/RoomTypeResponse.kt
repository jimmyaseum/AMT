package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class RoomTypeResponse (
    @SerializedName("Data")
    val Data: ArrayList<RoomTypeModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class RoomTypeModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("Title")
    val Title: String?,
    @SerializedName("Prefix")
    val Prefix: String?,
    @SerializedName("RoomTypeURL")
    val RoomTypeURL: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false

)



