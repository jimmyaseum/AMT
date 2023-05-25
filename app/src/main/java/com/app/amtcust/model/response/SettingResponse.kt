package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class SettingResponse (
        @SerializedName("Data")
        val Data: SettingModel? = null,
        @SerializedName("Details")
        val Details: Any?,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
    )
data class SettingModel (
        @SerializedName("MobileNo")
        val MobileNo: String,
        @SerializedName("Email")
        val Email: String
   )