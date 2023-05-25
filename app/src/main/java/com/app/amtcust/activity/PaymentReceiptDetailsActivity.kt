package com.app.amtcust.activity

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.app.amtcust.R
import com.app.amtcust.model.response.PaymentReceiptDetailsModel
import com.app.amtcust.model.response.PaymentReceiptDetailsResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import com.ibm.icu.text.RuleBasedNumberFormat
import kotlinx.android.synthetic.main.activity_payment_receipt_details.*
import kotlinx.android.synthetic.main.activity_payment_receipt_details.imgBack
import kotlinx.android.synthetic.main.activity_route_details.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PaymentReceiptDetailsActivity : BaseActivity() {
    var ID: Int = 0

    var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_receipt_details)

        ID = intent.getIntExtra("ID",0)
        initializeView()
    }
    override fun initializeView() {
        imgBack.setOnClickListener {
            finish()
        }

        callDetailApi()
    }

    private fun callDetailApi() {

        showProgress()
        var jsonObject = JSONObject()
        jsonObject.put("ID", ID)

        val call = ApiUtils.apiInterface.getPaymentListDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<PaymentReceiptDetailsResponse> {

            override fun onResponse(call: Call<PaymentReceiptDetailsResponse>, response: Response<PaymentReceiptDetailsResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<PaymentReceiptDetailsResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: PaymentReceiptDetailsModel) {

        if(arrayList.BookingNo != null && arrayList.BookingNo != "") {
            txtBookingNo.text =  arrayList.BookingNo
        }
        if(arrayList.PaymentDate != null && arrayList.PaymentDate != "") {
            txtDateofReceipt.text =  arrayList.PaymentDate
        }
        if(arrayList.BookBy != null && arrayList.BookBy != "") {
            txtBookBy.text =  arrayList.BookBy
        }
        if(arrayList.BranchName != null && arrayList.BranchName != "") {
            txtBranch.text =  arrayList.BranchName
        }
        if(arrayList.Name != null && arrayList.Name != "") {
            txtName.text =  arrayList.Name
        }
        if(arrayList.Address != null && arrayList.Address != "") {
            txtAddress.text =  arrayList.Address
        }
        if(arrayList.TourDateCode != null && arrayList.TourDateCode != "") {
            txtTourCode.text =  arrayList.TourDateCode
        }
        if(arrayList.StartEndDate != null && arrayList.StartEndDate != "") {
            txtStartEndDate.text =  arrayList.StartEndDate
        }
        if(arrayList.TotalNoOfPax != null && arrayList.TotalNoOfPax != "") {
            txtTotalPax.text =  arrayList.TotalNoOfPax
        }
        if(arrayList.FullTicket != null && arrayList.FullTicket != "") {
            txtFullTicket.text =  arrayList.FullTicket
        }
        if(arrayList.TotalCWB != null && arrayList.TotalCWB != "") {
            txtHTWS.text =  arrayList.TotalCWB
        }
        if(arrayList.TotalCNB != null && arrayList.TotalCNB != "") {
            txtHTWNS.text =  arrayList.TotalCNB
        }
        if(arrayList.TotalNoOfRooms != null && arrayList.TotalNoOfRooms != "") {
            txtNoofRoom.text =  arrayList.TotalNoOfRooms
        }
        if(arrayList.ExtraBed != null && arrayList.ExtraBed != "") {
            txtNoOfExtraBed.text =  arrayList.ExtraBed
        }
        if(arrayList.TotalNoOfSeats != null && arrayList.TotalNoOfSeats != "") {
            txtNoofSeat.text =  arrayList.TotalNoOfSeats
        }
        if(arrayList.VehicleType != null && arrayList.VehicleType != "") {
            txtVehicleType.text =  arrayList.VehicleType
        }
        if(arrayList.PaymentType != null && arrayList.PaymentType != "") {
            txtPaymentMode.text =  arrayList.PaymentType
        }
        if(arrayList.Amount != null && arrayList.Amount != "") {
            txtAmount.text =  arrayList.Amount
            val amount_word = convertIntoWords(arrayList.Amount.toDouble(), "en", "US")
            txtAmountInWords.text =  amount_word
        }
    }

    private fun convertIntoWords(str: Double, language: String, Country: String): String? {
        val local = Locale(language, Country)
        val ruleBasedNumberFormat = RuleBasedNumberFormat(local, RuleBasedNumberFormat.SPELLOUT)
        return ruleBasedNumberFormat.format(str)
    }
}