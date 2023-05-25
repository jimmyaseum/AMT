package com.app.amtcust.model

import com.app.amtcust.model.response.SectorListModel
import com.google.gson.annotations.SerializedName

data class CustomerAppVersion (
    @SerializedName("Data")
    val Data: Setting? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
    )
data class Setting (
    @SerializedName("Version")
    val Version: String
        )