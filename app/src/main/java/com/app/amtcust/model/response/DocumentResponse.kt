package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class DocumentResponse (
        @SerializedName("Data")
        val Data: ArrayList<DocumentListModel>? = null,
        @SerializedName("Details")
        val Details: Any?,
        @SerializedName("Message")
        val Message: String,
        @SerializedName("Status")
        val Status: Int
        )
data class DocumentListModel (
        @SerializedName("ID")
        val ID: Int?,
        @SerializedName("CustomerID")
        val CustomerID: Int?,
        @SerializedName("DocumentType")
        val DocumentType: String?,
        @SerializedName("DocumentNo")
        val DocumentNo: String?,
        @SerializedName("DocumentCopy")
        val DocumentCopy: String?

)
