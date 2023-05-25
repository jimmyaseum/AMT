package com.app.amtcust.activity

import android.os.Bundle
import com.app.amtcust.R
import com.app.amtcust.adapter.HotelImagePagerAdapter
import com.app.amtcust.adapter.voucher.GuestAdapter
import com.app.amtcust.model.response.HotelVoucherDetailsModel
import com.app.amtcust.model.response.HotelVoucherDetailsResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_hotel_details.*
import kotlinx.android.synthetic.main.activity_hotel_details.card2
import kotlinx.android.synthetic.main.activity_hotel_details.card3
import kotlinx.android.synthetic.main.activity_hotel_details.imgBack
import kotlinx.android.synthetic.main.activity_hotel_details.rvGuestList
import kotlinx.android.synthetic.main.activity_hotel_details.txtReadMore
import kotlinx.android.synthetic.main.activity_hotel_details.viewPager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HotelVoucherDetailsActivity : BaseActivity() {

//    var HotelID: Int = 0
//    var TourID: Int = 0
//    var VoucherNo: String = ""
    var HotelVoucherID: Int = 0

    var sharedPreference: SharedPreference? = null

    var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_details)

//        HotelID = intent.getIntExtra("HotelID",0)
//        TourID = intent.getIntExtra("TourID",0)
//        VoucherNo = intent.getStringExtra("VoucherNo").toString()

        HotelVoucherID = intent.getIntExtra("HotelVoucherID",0)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        txtReadMore.setOnClickListener {
            LL_More.visible()
            txtReadMore.invisible()
        }
        txtReadLess.setOnClickListener {
            LL_More.gone()
            txtReadMore.visible()
        }

        callDetailApi(/*HotelID , TourID , VoucherNo*/)
    }

    private fun callDetailApi(/*hotelID: Int, tourID : Int,voucherNo: String*/) {

        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        showProgress()
        var jsonObject = JSONObject()
//        jsonObject.put("HotelID", hotelID)
//        jsonObject.put("TourID", tourID)
//        jsonObject.put("VoucherNo", voucherNo)
//        jsonObject.put("CustomerID",userId)
        jsonObject.put("ID",HotelVoucherID)

        val call = ApiUtils.apiInterface.getHotelVoucherDetails(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<HotelVoucherDetailsResponse> {

            override fun onResponse(call: Call<HotelVoucherDetailsResponse>, response: Response<HotelVoucherDetailsResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<HotelVoucherDetailsResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: HotelVoucherDetailsModel) {

        if(arrayList.images != null) {
            val TourImagesP = arrayList.images
            if(TourImagesP!!.size > 0) {
                val adapter = HotelImagePagerAdapter(this, TourImagesP!!)
                viewPager!!.adapter = adapter

                pageIndicator.setViewPager(viewPager)
                val density = resources.displayMetrics.density

                pageIndicator.radius = 6 * density

                val timerTask: TimerTask = object : TimerTask() {
                    override fun run() {
                        viewPager.post {
                            viewPager.currentItem = (viewPager.currentItem + 1) % TourImagesP!!.size

                        }
                    }
                }
                timer = Timer()
                timer!!.schedule(timerTask, 3000, 3000)

            }
        }

        if(arrayList.HotelName != null && arrayList.HotelName != "") {
            txtHotelName.text =  arrayList.HotelName
        }
        if(arrayList.HotelAddress != null && arrayList.HotelAddress != "") {
            txtHotelAddress.text =  arrayList.HotelAddress
        }
        if(arrayList.PBN != null && arrayList.PBN != "") {
            txtHotelBokingNo.text =  arrayList.PBN
        }
        if(arrayList.HotelMobileNo != null && arrayList.HotelMobileNo != "") {
            txtHotelMobileNo.text =  arrayList.HotelMobileNo
        }
        if(arrayList.NoOfNights != null && arrayList.NoOfNights != 0) {
            txtNoOfNights.text =  arrayList.NoOfNights.toString()
        }
        if(arrayList.RoomType != null && arrayList.RoomType != "") {
            txtHotelRoomType.text =  arrayList.RoomType
        }
        if(arrayList.MealPlan != null && arrayList.MealPlan != "") {
            txtHotelMealPlan.text =  arrayList.MealPlan
        }
        if(arrayList.CheckinDate != null && arrayList.CheckinDate != "") {
            val checkinDate = convertDateStringToString(arrayList.CheckinDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM_YYYY)!!
            txtHotelCheckIn.text =  checkinDate
        }
        if(arrayList.CheckoutDate != null && arrayList.CheckoutDate != "") {
            val checkoutDate = convertDateStringToString(arrayList.CheckoutDate, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM_YYYY)!!
            txtHotelCheckOut.text =  checkoutDate
        }
        if(arrayList.BookBy != null && arrayList.BookBy != "") {
            txtHotelBookBy.text =  arrayList.BookBy
        }
        if(arrayList.ApprovedBy != null && arrayList.ApprovedBy != "") {
            txtHotelConfirmBy.text =  arrayList.ApprovedBy
        }
        if(arrayList.Supplier != null && arrayList.Supplier != "") {
             txtHotelSupplier.text =  arrayList.Supplier
        }

        if(arrayList.Manager != null && arrayList.Manager != "") {
            txtManagerName.text =  arrayList.Manager
            if(arrayList.ManagerEmail != null && arrayList.ManagerEmail != "") {
                txtManagerEmail.text =  arrayList.ManagerEmail
            }
            if(arrayList.ManagerMobileNo != null && arrayList.ManagerMobileNo != "") {
                txtManagerMobile.text =  arrayList.ManagerMobileNo
            }

        } else {
            card2.gone()
        }
        if(arrayList.guestDetails != null) {
            if(arrayList.guestDetails.size > 0) {
                rvGuestList.setHasFixedSize(true)
                val adapterGuestList = GuestAdapter(this, arrayList.guestDetails!!)
                rvGuestList.adapter  = adapterGuestList
                card3.visible()
            } else {
                card3.gone()
            }
        }
    }

}