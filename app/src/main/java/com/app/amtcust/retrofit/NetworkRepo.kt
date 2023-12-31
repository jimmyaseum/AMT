package com.app.amtcust.retrofit

import com.app.amtcust.model.response.*
import com.app.amtcust.utils.AppConstant.BASE_URL_APP
import com.app.amtcust.utils.enqueueCall
import com.app.amtcust.utils.getRequestJSONBody
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class NetworkRepo {

    private val mService: ApiInterface
    private var mRetrofit: Retrofit? = null

    init {
        mService = createService()
    }

    class OAuthInterceptor(private val tokenType: String, private val acceessToken: String): Interceptor {

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var request = chain.request()

            if (!isInternetAvailable()) {
                throw Exception()
            } else {
                val builder = request.newBuilder()
                builder?.let { builder ->
                    builder.addHeader("Authorization", "$tokenType $acceessToken")
                }
                request = builder.build()

                return chain.proceed(request)
            }
        }

        fun isInternetAvailable(): Boolean {
            return try {
                val ipAddr = InetAddress.getByName("google.com")
                //You can replace it with your name
                !ipAddr.equals("")

            } catch (e: Exception) {
                false
            }
        }

    }

    private val client: OkHttpClient

    get() {
            val dispatcher = Dispatcher()
            dispatcher.maxRequests = 1

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .dispatcher(dispatcher)
//                .addInterceptor(OAuthInterceptor("Bearer", AppConstant.TOKEN))
                .addNetworkInterceptor(interceptor) // comment
                .build()
        }

    private fun createService(): ApiInterface {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        if (mRetrofit == null) {
            mRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_APP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }
        return mRetrofit!!.create(ApiInterface::class.java)
    }

    fun getSectorList(
        regionid: String,
        listners: ResponseListner<SectorListResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("RegionID", regionid)

        mService.getSectorList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<SectorListResponse?>)
                }
            }
    }

    fun getFamilyMemberList(
        cusromer: Int,
        listners: ResponseListner<FamilyMemberResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("ParentCustomerID", cusromer)

        mService.getFamilyMemberList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<FamilyMemberResponse?>)
                }
            }
    }

    fun getPaymentReceiptList(
        cusromerid: Int,
        listners: ResponseListner<PaymentListResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", cusromerid)

        mService.getPaymentList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<PaymentListResponse?>)
                }
            }
    }


    fun getTopIndianList(
        listners: ResponseListner<TopIndiaListResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("OperationType", 1)

        mService.getTopIndianList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TopIndiaListResponse?>)
                }
            }
    }

    fun getTopTrendingHolidayList(
        listners: ResponseListner<TopTrendListResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("OperationType", 2)

        mService.getTopTrendList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TopTrendListResponse?>)
                }
            }
    }

    fun getToursList(
        listners: ResponseListner<TourListResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("OperationType", 3)

        mService.getTourList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TourListResponse?>)
                }
            }
    }

    fun getCustomizedHolidaysList(
        listners: ResponseListner<CustomizedListResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("OperationType", 4)

        mService.getCustomizedList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<CustomizedListResponse?>)
                }
            }
    }


    fun getHimalayanList(
        listners: ResponseListner<HimalayanListResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("OperationType", 5)

        mService.getHimalayanList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<HimalayanListResponse?>)
                }
            }
    }


    fun getDocumentList(
        cusromer: Int,
        doctype: String,
        listners: ResponseListner<DocumentResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", cusromer)
        jsonObject.put("DocumentType", doctype)

        mService.getDocumentList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<DocumentResponse?>)
                }
            }
    }

    fun getTourDestinationList(
        RegionID: String,
        TravelType: String,
        IsHimalayanTreks: String,
        SectorID: String,
        Rate: String,
        NoOfDays: String,
        SpecialityType: String,
        OrderBy: String,
        IsSearch: String,
        listners: ResponseListner<TourDestinationResponse?>
    ) {

        val mRate = Rate.replace(", " , " OR ")
        val mNoOfDays = NoOfDays.replace(", " , " OR ")

        var jsonObject = JSONObject()
        jsonObject.put("RegionID", RegionID)
        jsonObject.put("TravelType", TravelType)
        jsonObject.put("IsHimalayanTreks", IsHimalayanTreks)
        jsonObject.put("SectorID", SectorID)
        jsonObject.put("Rate", mRate)
        jsonObject.put("NoOfDays", mNoOfDays)
        jsonObject.put("SpecialityType", SpecialityType)
        jsonObject.put("OrderBy", OrderBy)
        jsonObject.put("SectorName", IsSearch)

        mService.getTourListFilter(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TourDestinationResponse?>)
                }
            }
    }

    fun getTourDetailsList(
        tourid: Int,
        ratetype:String,
        noofnights: Int,
        roomTypeID: Int,
        listners: ResponseListner<TourDetailsResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("TourID", tourid)
        jsonObject.put("RateType", ratetype)
        jsonObject.put("NoOfNights", noofnights.toString())
        jsonObject.put("RoomTypeID",roomTypeID)

        mService.getTourDetail(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TourDetailsResponse?>)
                }
            }
    }

    fun getTripList(
        cusromer: Int,
        opty: Int,
        listners: ResponseListner<TourBookingResponse?>
    ) {
        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", cusromer)
//        jsonObject.put("OperationType", opty)

        mService.getTourBookingList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TourBookingResponse?>)
                }
            }
    }

    fun getTourInformation(
        tourbookingid: Int,
        listners: ResponseListner<TourInformationResponse?>
    ) {
        var jsonObject = JSONObject()
        jsonObject.put("ID", tourbookingid)

        mService.getTourInformation(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TourInformationResponse?>)
                }
            }
    }


    fun getPrevious(
        cusromer: Int,
        opty: Int,
        listners: ResponseListner<FlightVoucherResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", cusromer)
        jsonObject.put("OperationType", opty)

        mService.getPreviousList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<FlightVoucherResponse?>)
                }
            }
    }

    fun getUpcomingHotel(
        cusromer: Int,
        opty: Int,
        listners: ResponseListner<UpcomingHotelResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", cusromer)
        jsonObject.put("OperationType", opty)

        mService.getUpcomingHotelList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<UpcomingHotelResponse?>)
                }
            }
    }

    fun getTourBookingDetailsList(
        tourid: Int,
        listners: ResponseListner<TourBookingDetailsResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("ID", tourid)

        mService.getTourBookingDetail(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<TourBookingDetailsResponse?>)
                }
            }
    }


    fun getFutureRouteList(
        cusromer: Int,
        opty: Int,
        listners: ResponseListner<RouteVoucherResponse?>
    ) {

        var jsonObject = JSONObject()
        jsonObject.put("CustomerID", cusromer)
        jsonObject.put("OperationType", opty)

        mService.getRouteList(getRequestJSONBody(jsonObject.toString()))
            .enqueueCall {
                onResponse = {
                    listners.onResponse(it as ApiResponse<RouteVoucherResponse?>)
                }
            }
    }


}