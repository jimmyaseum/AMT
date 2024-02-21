package com.app.amtcust.model

import com.google.gson.annotations.SerializedName

data class AttachmentModel (
    @SerializedName("TourID")
    var TourID: Int? = null,
    @SerializedName("Title")
    var Title: String? = null,
    @SerializedName("Documents")
    var Documents: String? = null,
    @SerializedName("CreatedBy")
    var CreatedBy: Int? = null,
    @SerializedName("UpdatedBy")
    var UpdatedBy: Int? = null,
    @SerializedName("CreatedOn")
    var CreatedOn: String? = null,
    @SerializedName("UpdatedOn")
    var UpdatedOn: String? = null,
    @SerializedName("ID")
    var ID: Int? = null,
    @SerializedName("Message")
    var Message: String? = null,
    @SerializedName("IsActive")
    var IsActive: Boolean? = null,
    @SerializedName("IsDelete")
    var IsDelete: Boolean? = null,
    @SerializedName("Details")
    var Details: String? = null
)