package com.app.amtcust.retrofit

import com.app.amtcust.Chat.Nofification.PushGroupNotification
import com.app.amtcust.Chat.Nofification.PushNotification
import com.app.amtcust.model.AirlineVoucherDetailsResponse
import com.app.amtcust.model.CustomerAppVersion
import com.app.amtcust.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * Created by Jimmy
 */
interface ApiInterface {

    @POST("Login/VersionApi")
    fun getCustomerAppVersion(@Body body: RequestBody?): Call<CustomerAppVersion>

    @POST("Sector/FindAllItems")
    fun getAllSector(@Body body: RequestBody?): Call<SectorListResponse>

    @POST("Customers/Resigtartion")
    fun registration(@Body body: RequestBody?): Call<RegistrationResponse>

    @POST("Login/Login")
    fun login(@Body body: RequestBody?): Call<LoginResponse>

    @POST("Login/Logout")
    fun logout(@Body body: RequestBody?): Call<CommonResponse>

    @POST("Login/OTPVerification")
    fun VerifyOTP(@Body body: RequestBody?): Call<RegistrationResponse>

    @GET("City/FindAll")
    fun getAllCity(): Call<CityResponse>

    @GET("Relation/findall")
    fun getAllRelation(): Call<RelationResponse>

    @POST("Sector/SectorByRegion")
    fun getSectorList(@Body body: RequestBody?): Call<SectorListResponse>

    @Multipart
    @POST("Customers/Insert") // done
    fun CustomerAdd(
        @Query("Initials") Initials: String,
        @Query("FirstName") FirstName: String,
        @Query("LastName") LastName: String,
        @Query("MobileNo") MobileNo: String,
        @Query("ParentCustomerID") ParentCustomerID: Int,
        @Query("RelationshipID") RelationshipID: Int,
        @Query("Address") Address: String,
        @Query("EmailID") EmailID: String,
        @Query("ResidentPhoneNo") ResidentPhoneNo: String,
        @Query("TravellingMobileNo") TravellingMobileNo: String,
        @Query("EmergencyNo") EmergencyNo: String,
        @Query("DOB") DOB: String,
        @Query("Gender") Gender: String,
        @Query("Pincode") Pincode: String,
        @Query("CityID") CityID: Int,
        @Query("StateID") StateID: Int,
        @Query("CountryID") CountryID: Int,
        @Query("IsActive") IsActive: Boolean,
        @Query("CreatedBy") CreatedBy: Int,
        @Part image: ArrayList<MultipartBody.Part>?
    ): Call<CommonResponse>

    @Multipart
    @POST("Customers/Update") // done
    fun CustomerUpdate(
        @Query("ID") ID: Int,
        @Query("Initials") Initials: String,
        @Query("FirstName") FirstName: String,
        @Query("LastName") LastName: String,
        @Query("MobileNo") MobileNo: String,
        @Query("ParentCustomerID") ParentCustomerID: Int,
        @Query("RelationshipID") RelationshipID: Int,
        @Query("Address") Address: String,
        @Query("EmailID") EmailID: String,
        @Query("ResidentPhoneNo") ResidentPhoneNo: String,
        @Query("TravellingMobileNo") TravellingMobileNo: String,
        @Query("EmergencyNo") EmergencyNo: String,
        @Query("DOB") DOB: String,
        @Query("Gender") Gender: String,
        @Query("Pincode") Pincode: String,
        @Query("CityID") CityID: Int,
        @Query("StateID") StateID: Int,
        @Query("CountryID") CountryID: Int,
        @Query("IsActive") IsActive: Boolean,
        @Query("UpdatedBy") CreatedBy: Int,
        @Query("OperationType") OperationType: Int,
        @Part image: ArrayList<MultipartBody.Part>?
    ): Call<CommonResponse>

    @GET("Customers/FindByID")
    fun getDetailByCustomers(@Query("id") _id: Int): Call<CustomerResponse> // done

    @POST("Customers/OngoingTourData")
    fun getCustomersOngoingTrip(@Body body: RequestBody?): Call<OngoingTourDataResponse> // done

    @POST("Customers/CustomerEmployeeData")
    fun getCustomersEmployeeData(@Body body: RequestBody?): Call<EmployeeDataResponse> // done

    @POST("Customers/FamilyMembersView")
    fun getFamilyMemberList(@Body body: RequestBody?): Call<FamilyMemberResponse>

    @Multipart
    @POST("Customers/UpdateCompany") // done
    fun CustomerCompanyUpdate(
        @Query("ID") ID: Int,
        @Query("CompanyName") CompanyName: String,
        @Query("CompanyAddress") CompanyAddress: String,
        @Query("CompanyMobileNo") CompanyMobileNo: String,
        @Query("CompanyEmailID") CompanyEmailID: String,
        @Query("CompanyGSTNo") CompanyGSTNo: String,
        @Query("CompanyPanNo") CompanyPanNo: String,
        @Query("CompanyPincode") CompanyPincode: String,
        @Query("CompanyCityID") CityID: Int,
        @Query("CompanyStateID") StateID: Int,
        @Query("CompanyCountryID") CountryID: Int,
        @Query("IsActive") IsActive: Boolean,
        @Query("UpdatedBy") CreatedBy: Int,
        @Part imagegst: MultipartBody.Part,
        @Part imagepan: MultipartBody.Part
    ): Call<CommonResponse>


    @POST("HomePage/FindAll")
    fun getTopIndianList(@Body body: RequestBody?): Call<TopIndiaListResponse>

    @POST("HomePage/FindAll")
    fun getTopTrendList(@Body body: RequestBody?): Call<TopTrendListResponse>

    @POST("HomePage/FindTopSelling")
    fun getTourList(@Body body: RequestBody?): Call<TourListResponse>

    @POST("HomePage/FindTopSelling")
    fun getCustomizedList(@Body body: RequestBody?): Call<CustomizedListResponse>

    @POST("HomePage/FindTopSelling")
    fun getHimalayanList(@Body body: RequestBody?): Call<HimalayanListResponse>

    @Multipart
    @POST("Customers/CustomerDocuments") // done
    fun CustomerDocumetnUpload(
        @Query("CustomerID") CustomerID: Int,
        @Query("DocumentType") DocumentType: String,
        @Query("DocumentNo") DocumentNo: String,
        @Query("ValidTill") ValidTill: String,
        @Query("IsActive") IsActive: Boolean,
        @Query("CreatedBy") CreatedBy: Int,
        @Part imagegst: MultipartBody.Part
    ): Call<CommonResponse>

    @POST("customers/documentslist")
    fun getDocumentList(@Body body: RequestBody?): Call<DocumentResponse>

    @POST("Customers/Delete")
    fun getCustomersDelete(@Body body: RequestBody?): Call<CommonResponse> // done

    @GET("SpecialityType/FindAll")
    fun getSpecialityType(): Call<SpecialityResponse>

    @POST("Tour/TourList")
    fun getTourListFilter(@Body body: RequestBody?): Call<TourDestinationResponse>

    @POST("Tour/TourDetails")
    fun getTourDetail(@Body body: RequestBody?): Call<TourDetailsResponse>

    @POST("TourBooking/MyTrips")
    fun getTourBookingList(@Body body: RequestBody?): Call<TourBookingResponse>

    @POST("TourBooking/TourInformation")
    fun getTourInformation(@Body body: RequestBody?): Call<TourInformationResponse>

    @POST("TourBooking/TourPersonalInformation")
    fun AddTourPersonalInformation(@Body body: RequestBody?): Call<CommonResponse>

    @POST("Voucher/AirlineVoucherList")
    fun getPreviousList(@Body body: RequestBody?): Call<FlightVoucherResponse>

    @POST("Voucher/AirlineVoucherDetails")
    fun getAirlineVoucherDetails(@Body body: RequestBody?): Call<AirlineVoucherDetailsResponse>

    @POST("Voucher/HotelVoucherList")
    fun getUpcomingHotelList(@Body body: RequestBody?): Call<UpcomingHotelResponse>

    @POST("Voucher/HotelVoucherDetails")
    fun getHotelVoucherDetails(@Body body: RequestBody?): Call<HotelVoucherDetailsResponse>

    @POST("TourBooking/TourPaxInfo")
    fun getTourPaxInformation(@Body body: RequestBody?): Call<TourPaxInformationResponse>

    @POST("TourBooking/UpdateTourPax")
    fun UpdateTourPax(@Body body: RequestBody?): Call<CommonResponse>

    @POST("TourBooking/TourBookingDetails")
    fun getTourBookingDetail(@Body body: RequestBody?): Call<TourBookingDetailsResponse>

    @POST("Voucher/RouteVoucherList")
    fun getRouteList(@Body body: RequestBody?): Call<RouteVoucherResponse>

    @POST("Voucher/RouteVoucherDetails")
    fun getRouteVoucherDetails(@Body body: RequestBody?): Call<RouteVoucherDetailsResponse>

    @GET("Setting/FindAll")
    fun getSetting(): Call<SettingResponse> // done

    @GET("TourDates/getupcomingtourdates")
    fun getTourDates(@Query("TourID") _id: Int): Call<TourMonthResponse> // done

    @POST("Notification/FindAll")
    fun getNotificationList(@Body body: RequestBody?): Call<NotificationListResponse>

    @POST("Inquiries/Insert")
    fun CreateInquiry(@Body body: RequestBody?): Call<CommonResponse>

    @POST("PaymentList/CustomerPaymentList")
    fun getPaymentList(@Body body: RequestBody?): Call<PaymentListResponse>

    @POST("PaymentList/CustomerPaymentDetails")
    fun getPaymentListDetails(@Body body: RequestBody?): Call<PaymentReceiptDetailsResponse>

    // region Send FCM Notification in CHAT
    @Headers("Content-Type:application/json",
    "Authorization:key=AAAAMlaPWSA:APA91bEPIiMadJjbvhWA4hND_5f_Tx_tyCt3HMCpsZuCEO_rHKvG-q6yPbQ7ygvzVlpi3NBANhksegpQLnE6TMEavGVgvYHhC6m-8qTevSxxdSlz1rEUdI_Lsi1c3YPtKhJWtGckqmVs")
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification): Call<PushNotification>


    // region Send FCM Notification in CHAT
    @Headers("Content-Type:application/json",
        "Authorization:key=AAAAMlaPWSA:APA91bEPIiMadJjbvhWA4hND_5f_Tx_tyCt3HMCpsZuCEO_rHKvG-q6yPbQ7ygvzVlpi3NBANhksegpQLnE6TMEavGVgvYHhC6m-8qTevSxxdSlz1rEUdI_Lsi1c3YPtKhJWtGckqmVs")
    @POST("fcm/send")
    fun sendGroupNotification(@Body notification: PushGroupNotification): Call<PushNotification>

    @POST("TourBooking/TourBookingPaxDetails")
    fun getTourBookingPaxDetails(@Body body: RequestBody?): Call<TourBookingPaxResponse>

    @Multipart
    @POST("PaymentList/PaymentReceiptInsert") // done
    fun AddPaymentReceipt(
        @Part("BookingNo") BookingNo: RequestBody?,
        @Part("PaymentFor") PaymentFor: RequestBody,
        @Part("PaymentDate") PaymentDate: RequestBody?,
        @Part("Amount") Amount: RequestBody?,
        @Part("PaymentType") PaymentType: RequestBody?,
        @Part ReceiptImage: ArrayList<MultipartBody.Part>?
    ): retrofit2.Call<CommonResponse>

    @Multipart
    @POST("PaymentList/PaymentReceiptUpdate") // done
    fun EditPaymentReceipt(
        @Part("ID") ID: RequestBody?,
        @Part("BookingNo") BookingNo: RequestBody?,
        @Part("PaymentFor") PaymentFor: RequestBody,
        @Part("PaymentDate") PaymentDate: RequestBody?,
        @Part("Amount") Amount: RequestBody?,
        @Part("PaymentType") PaymentType: RequestBody?,
        @Part ReceiptImage: ArrayList<MultipartBody.Part>?
    ): retrofit2.Call<CommonResponse>


}