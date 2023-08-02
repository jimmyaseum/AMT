package com.app.amtcust.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.app.amtcust.R
import com.app.amtcust.adapter.voucher.PassengersAdapter
import com.app.amtcust.model.AirlineVoucherDetailsModel
import com.app.amtcust.model.AirlineVoucherDetailsResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_flight_details.*
import kotlinx.android.synthetic.main.activity_flight_details.imgBack
import kotlinx.android.synthetic.main.adapter_flight_voucher_list.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FlightVoucherDetailActivity : BaseActivity() {

    var AirlineID: Int = 0
    var AirlineDocument : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)

        AirlineID = intent.getIntExtra("ID",0)

        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }
        callDetailApi(AirlineID)
        LLViewDocument.setOnClickListener {
            if(isOnline(this)) {
                if(AirlineDocument.contains(".pdf")) {
                    var format = "https://docs.google.com/gview?embedded=true&url=%s"
                    val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, AirlineDocument)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                    startActivity(browserIntent)
                } else {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AirlineDocument))
                    startActivity(browserIntent)
                }
            } else {
                toast(resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
            }
        }

    }

    private fun callDetailApi(airlineVoucherID: Int) {

        showProgress()
        var jsonObject = JSONObject()
        jsonObject.put("ID", airlineVoucherID)

        val call = ApiUtils.apiInterface.getAirlineVoucherDetails(getRequestJSONBody(jsonObject.toString()))
        call.enqueue(object : Callback<AirlineVoucherDetailsResponse> {

            override fun onResponse(call: Call<AirlineVoucherDetailsResponse>, response: Response<AirlineVoucherDetailsResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<AirlineVoucherDetailsResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: AirlineVoucherDetailsModel) {

        if(arrayList.JourneyType != null && arrayList.JourneyType != "") {
            txtJournyType.text = arrayList.JourneyType
        }
        if(arrayList.PNRNo != null && arrayList.PNRNo != "") {
            txtONPNRNo.text = arrayList.PNRNo
        }
        if(arrayList.AirlineName != null && arrayList.AirlineName != "") {
            txtONAirLineName.text = arrayList.AirlineName
        }
        if(arrayList.FromAirportCode != null && arrayList.FromAirportCode != "") {
            txtONFromAirportCode.text = arrayList.FromAirportCode
        }
        if(arrayList.FromAirport != null && arrayList.FromAirport != "") {
            txtONFromAirportName.text = arrayList.FromAirport
        }
        if(arrayList.ToAirportCode != null && arrayList.ToAirportCode != "") {
            txtONToAirportCode.text = arrayList.ToAirportCode
        }
        if(arrayList.ToAirport != null && arrayList.ToAirport != "") {
            txtONToAirportName.text = arrayList.ToAirport
        }
        if(arrayList.DepartureTime != null && arrayList.DepartureTime != "") {
            val departureTime = convertDateStringToString(arrayList.DepartureTime!!, AppConstant.HH_MM_FORMAT, AppConstant.HH_MM_AA_FORMAT)!!
            txtONDeparTime.text = departureTime
        }
        if(arrayList.ArrivalTime != null && arrayList.ArrivalTime != "") {
            val arrivalTime = convertDateStringToString(arrayList.ArrivalTime!!, AppConstant.HH_MM_FORMAT, AppConstant.HH_MM_AA_FORMAT)!!
            txtONArriveTime.text = arrivalTime
        }
        if(arrayList.DepartureDate != null && arrayList.DepartureDate != "") {
            val departureDate = convertDateStringToString(arrayList.DepartureDate!!, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM)!!
            txtONDeparDate.text = departureDate
        }
        if(arrayList.ArrivalDate != null && arrayList.ArrivalDate != "") {
            val arrivalDate = convertDateStringToString(arrayList.ArrivalDate!!, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM)!!
            txtONArriveDate.text = arrivalDate
        }
        if(arrayList.passengers != null) {
            if(arrayList.passengers.size > 0) {
                rvPassengerList.setHasFixedSize(true)
                val adapterList = PassengersAdapter(this, arrayList.passengers!!)
                rvPassengerList.adapter  = adapterList
            }
        }

        if(arrayList.AirlineVoucherTicket != null && arrayList.AirlineVoucherTicket != "") {
            AirlineDocument = arrayList.AirlineVoucherTicket
            LLViewDocument.visible()
        } else {
            LLViewDocument.gone()
        }
    }

}