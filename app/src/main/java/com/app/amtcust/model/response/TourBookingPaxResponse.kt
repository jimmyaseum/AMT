package com.app.amtcust.model.response


import android.net.Uri
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.annotations.SerializedName

data class TourBookingPaxResponse (
    @SerializedName("Data")
    val Data: TourBookingPaxModel? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)

data class TourBookingPaxModel (
    @SerializedName("Name")
    val Name: String? = null,
    @SerializedName("MobileNo")
    val MobileNo: String? = null
)

data class PlaceInfo (
    @SerializedName("SectorID")
    val SectorID: Int? = null,
    @SerializedName("CityID")
    val CityID: Int? = null,
    @SerializedName("City")
    val City: String? = null
)

data class PlaceDataModel (
    @SerializedName("placeid")
    var placeid: Int = 0,

    @SerializedName("place")
    var place: String = "",

    @SerializedName("document")
    var document: Uri? = null,

    @SerializedName("documentname")
    var documentname: String? = null,

    //TIL Error handling
    @SerializedName("tilCity")
    var tilCity: TextInputLayout? = null,

    @SerializedName("tilUploadDocument")
    var tilUploadDocument: TextInputLayout? = null,

    @SerializedName("select_image")
    var select_image: ImageView? = null,

    @SerializedName("ll_Pdf")
    var ll_Pdf: LinearLayout? = null,

    @SerializedName("select_pdf")
    var select_pdf: TextView? = null
)