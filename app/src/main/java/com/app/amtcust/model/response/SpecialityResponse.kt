package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class SpecialityResponse(
    @SerializedName("Data")
    val Data: ArrayList<SpecialityListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class SpecialityListModel (
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("Title")
    val Title: String?,
    @SerializedName("Prefix")
    val Prefix: String?,
    @SerializedName("SpecialityURL")
    val SpecialityURL: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)
