package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class FamilyMemberResponse(
    @SerializedName("Data")
    val Data: ArrayList<FamilyMemberListModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class FamilyMemberListModel (
    @SerializedName("ID")
    val ID: Int = 0,
    @SerializedName("FirstName")
    val FirstName: String? = "",
    @SerializedName("LastName")
    val LastName: String? = "",
    @SerializedName("MobileNo")
    val MobileNo: String? = "",
    @SerializedName("ParentCustomerID")
    val ParentCustomerID: Int? = 0,
    @SerializedName("RelationshipID")
    val RelationshipID: Int? = 0,
    @SerializedName("Relation")
    val Relation: String? = "",
    @SerializedName("CustomerImage")
    val CustomerImage: String? = "",
    @SerializedName("EmailID")
    val EmailID: String? = ""
 )
