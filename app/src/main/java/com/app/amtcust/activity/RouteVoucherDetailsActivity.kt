package com.app.amtcust.activity

import android.os.Bundle
import com.app.amtcust.R
import com.app.amtcust.adapter.voucher.RouteAdapter
import com.app.amtcust.model.response.RouteVoucherDetailsModel
import com.app.amtcust.model.response.RouteVoucherDetailsResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_route_details.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteVoucherDetailsActivity : BaseActivity() {

//    var TourBookingNo: String = ""
    var ID: Int = 0
    var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

//        TourBookingNo = intent.getStringExtra("TourBookingNo").toString()
        ID = intent.getIntExtra("ID",0)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        callDetailApi(ID)

    }

    private fun callDetailApi(tourID : Int) {

        sharedPreference = SharedPreference(this)
//        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        showProgress()
        var jsonObject = JSONObject()
        jsonObject.put("ID", ID)
//        jsonObject.put("TourBookingNo", TourBookingNo)
//        jsonObject.put("CustomerID",userId)

        val call = ApiUtils.apiInterface.getRouteVoucherDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<RouteVoucherDetailsResponse> {

            override fun onResponse(call: Call<RouteVoucherDetailsResponse>, response: Response<RouteVoucherDetailsResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<RouteVoucherDetailsResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: ArrayList<RouteVoucherDetailsModel>) {
        if(arrayList[0].TourBookingNo != null && arrayList[0].TourBookingNo != "") {
            txtRouteBokingNo.text =  arrayList[0].TourBookingNo
        }
        if(arrayList[0].VoucherDate != null && arrayList[0].VoucherDate != "") {
            txtRouteDate.text =  arrayList[0].VoucherDate
        }
        if(arrayList[0].GuestName != null && arrayList[0].GuestName != "") {
            txtGuestName.text =  arrayList[0].GuestName
        }
        if(arrayList[0].StartDate != null && arrayList[0].StartDate != "") {
            txtStartDate.text =  arrayList[0].StartDate
        }
        if(arrayList[0].EndDate != null && arrayList[0].EndDate != "") {
            txtEndDate.text =  arrayList[0].EndDate

        }

        if(arrayList[0].TransporterName != null && arrayList[0].TransporterName != "") {
            txtTransporterName.text =  arrayList[0].TransporterName
        }
        if(arrayList[0].TransporterMobileNo1 != null && arrayList[0].TransporterMobileNo1 != "") {
            txtNo.text =  arrayList[0].TransporterMobileNo1
        }
        if(arrayList[0].VehicleType != null && arrayList[0].VehicleType != "") {
            txtVehicle.text =  arrayList[0].VehicleType
        }

        if(arrayList[0].route != null) {
            if(arrayList[0].route!!.size > 0) {
                rvGuestList.setHasFixedSize(true)
                val adapterRouteList = RouteAdapter(this, arrayList[0].route!!)
                rvGuestList.adapter  = adapterRouteList

            }
        }
    }
}