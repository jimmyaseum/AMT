package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @SerializedName("Status")
    var code: Int = 0,

    @SerializedName("Message")
    var message: String = "",

    @SerializedName("Data")
    var data: RegistrationModel? = null,

    @SerializedName("Details")
    var details: String = ""

)
data class RegistrationModel (
    @SerializedName("ID")
    var ID: Int = 0,

    @SerializedName("Name")
    var Name: String = "",

    @SerializedName("FirstName")
    var FirstName: String = "",

    @SerializedName("LastName")
    var LastName: String = "",

    @SerializedName("EmailID")
    var EmailID: String = "",

    @SerializedName("MobileNo")
    var MobileNo: String = "",

    @SerializedName("Details")
    var Details: String = "",

    @SerializedName("Token")
    var Token: String = "",

    @SerializedName("CustomerImage")
    var CustomerImage: String = ""
)

