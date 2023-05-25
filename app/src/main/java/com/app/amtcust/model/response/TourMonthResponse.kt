package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TourMonthResponse(
    @SerializedName("Data")
    val Data: ArrayList<TourMonthModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TourMonthModel (
    @SerializedName("monthData")
    val monthData: ArrayList<MonthDataModel>? = null
        )


data class MonthDataModel (
    @SerializedName("Month")
    val Month: Int?,
    @SerializedName("MonthName")
    val MonthName: String?,
    @SerializedName("tourdateData")
    val DateData: ArrayList<DateDataModel>? = null,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)

data class DateDataModel(
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("TourDate")
    val TourDate: String?,
    @SerializedName("MonthID")
    val MonthID: Int?,
    @SerializedName("TourDateCode")
    val TourDateCode: String?,
    @SerializedName("isSelected")
    var isSelected: Boolean = false
)
