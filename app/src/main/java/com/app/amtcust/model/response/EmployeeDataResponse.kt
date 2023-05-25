package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class EmployeeDataResponse (
    @SerializedName("Data")
    val Data: ArrayList<EmployeeDataModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class EmployeeDataModel (
    //general information
    @SerializedName("ID")
    val ID: Int = 0,
    @SerializedName("Name")
    val Name: String = "",
    @SerializedName("UserType")
    val UserType: String = "",
    @SerializedName("UserTypeID")
    val UserTypeID: Int = 0,
    @SerializedName("EmailID")
    val EmailID: String = "",
    @SerializedName("MobileNo")
    val MobileNo: String = ""
)
