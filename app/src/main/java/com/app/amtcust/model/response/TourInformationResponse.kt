package com.app.amtcust.model.response

import com.google.gson.annotations.SerializedName

data class TourInformationResponse (
    @SerializedName("Data")
    val Data: ArrayList<TourInformationModel>? = null,
    @SerializedName("Details")
    val Details: Any?,
    @SerializedName("Message")
    val Message: String,
    @SerializedName("Status")
    val Status: Int
)
data class TourInformationModel(
    @SerializedName("ID")
    val ID: Int?,
    @SerializedName("SectorID")
    val SectorID: Int?,
    @SerializedName("SectorName")
    val SectorName: String?,
    @SerializedName("SectorType")
    val SectorType: String?,
    @SerializedName("SubSectorID")
    val SubSectorID: Int?,
    @SerializedName("SubSectorName")
    val SubSectorName: String?,
    @SerializedName("TourID")
    val TourID: Int?,
    @SerializedName("PBN")
    val PBN: String?,
    @SerializedName("TourName")
    val TourName: String?,
    @SerializedName("TourStartDate")
    val TourStartDate: String?,
    @SerializedName("TourEndDate")
    val TourEndDate: String?,
    @SerializedName("NoOfNights")
    val NoOfNights: Int?,
    @SerializedName("RoomTypeID")
    val RoomTypeID: Int?,
    @SerializedName("RoomType")
    val RoomType: String?,
    @SerializedName("VehicleSharingPaxID")
    val VehicleSharingPaxID: Int?,
    @SerializedName("VehicleSharing")
    val VehicleSharing: String?,
    @SerializedName("TotalNoOfRooms")
    val TotalNoOfRooms: Int?,
    @SerializedName("TotalNoOfSeats")
    val TotalNoOfSeats: Int?,
    @SerializedName("FirstName")
    val FirstName: String?,
    @SerializedName("LastName")
    val LastName: String?,
    @SerializedName("MobileNo")
    val MobileNo: String?,
    @SerializedName("Address")
    val Address: String?,
    @SerializedName("CityID")
    val CityID: Int?,
    @SerializedName("City")
    val CityName: String?,
    @SerializedName("StateID")
    val StateID: Int?,
    @SerializedName("State")
    val StateName: String?,
    @SerializedName("CountryID")
    val CountryID: Int?,
    @SerializedName("Country")
    val CountryName: String?,
    @SerializedName("MobileNoDuringTravelling")
    val MobileNoDuringTravelling: String?,
    @SerializedName("EmailID")
    val EmailID: String?,
    @SerializedName("ResidentPhoneNo")
    val ResidentPhoneNo: String?,
    @SerializedName("EmergencyNo")
    val EmergencyNo: String?,
    @SerializedName("PANCardNo")
    val PANCardNo: String?,
    @SerializedName("PassportNo")
    val PassportNo: String?,
    @SerializedName("AadharNo")
    val AadharNo: String?,
    @SerializedName("IsCompanyInvoice")
    val IsCompanyInvoice: Boolean = false,
    @SerializedName("CompanyName")
    val CompanyName: String?,
    @SerializedName("CompanyAddress")
    val CompanyAddress: String?,
    @SerializedName("CompanyGSTNo")
    val CompanyGSTNo: String?,
    @SerializedName("CompanyPANNo")
    val CompanyPANNo: String?,
    @SerializedName("CompanyCityID")
    val CompanyCityID: Int?,
    @SerializedName("CompanyCity")
    val CompanyCityName: String?,
    @SerializedName("CompanyStateID")
    val CompanyStateID: Int?,
    @SerializedName("CompanyState")
    val CompanyStateName: String?,
    @SerializedName("CompanyCountryID")
    val CompanyCountryID: Int?,
    @SerializedName("CompanyCountry")
    val CompanyCountryName: String?

)

