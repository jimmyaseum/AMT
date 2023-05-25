package com.app.amtcust.activity

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.app.amtcust.R
import com.app.amtcust.adapter.*
import com.app.amtcust.adapter.voucher.FlightVoucherListAdapter
import com.app.amtcust.adapter.voucher.RouteVoucherListAdapter
import com.app.amtcust.adapter.voucher.UpcomingHotelListAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.*
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.tour_booking_details.*
import kotlinx.android.synthetic.main.tour_booking_details.LL_COST
import kotlinx.android.synthetic.main.tour_booking_details.LL_HOTEL
import kotlinx.android.synthetic.main.tour_booking_details.LL_INEX
import kotlinx.android.synthetic.main.tour_booking_details.LL_ITINERARY
import kotlinx.android.synthetic.main.tour_booking_details.LL_OVERVIEW
import kotlinx.android.synthetic.main.tour_booking_details.edtDate
import kotlinx.android.synthetic.main.tour_booking_details.edtHotelType
import kotlinx.android.synthetic.main.tour_booking_details.imgBack
import kotlinx.android.synthetic.main.tour_booking_details.linear_highlights
import kotlinx.android.synthetic.main.tour_booking_details.linear_overview
import kotlinx.android.synthetic.main.tour_booking_details.linear_price_of_inclusive
import kotlinx.android.synthetic.main.tour_booking_details.ns
import kotlinx.android.synthetic.main.tour_booking_details.pageIndicator
import kotlinx.android.synthetic.main.tour_booking_details.rvFacilityData
import kotlinx.android.synthetic.main.tour_booking_details.rvHotelCostData
import kotlinx.android.synthetic.main.tour_booking_details.rvHotelData
import kotlinx.android.synthetic.main.tour_booking_details.rvItineraryData
import kotlinx.android.synthetic.main.tour_booking_details.txtCities
import kotlinx.android.synthetic.main.tour_booking_details.txtDays
import kotlinx.android.synthetic.main.tour_booking_details.txtExclusions
import kotlinx.android.synthetic.main.tour_booking_details.txtHighlights
import kotlinx.android.synthetic.main.tour_booking_details.txtHighlightsMore
import kotlinx.android.synthetic.main.tour_booking_details.txtInclusions
import kotlinx.android.synthetic.main.tour_booking_details.txtNights
import kotlinx.android.synthetic.main.tour_booking_details.txtOverViewLess
import kotlinx.android.synthetic.main.tour_booking_details.txtOverViewLessIn_Ex
import kotlinx.android.synthetic.main.tour_booking_details.txtOverViewMore
import kotlinx.android.synthetic.main.tour_booking_details.txtOverViewMoreIn_Ex
import kotlinx.android.synthetic.main.tour_booking_details.txtRate
import kotlinx.android.synthetic.main.tour_booking_details.txtReadMore
import kotlinx.android.synthetic.main.tour_booking_details.txtReadMoreHighlights
import kotlinx.android.synthetic.main.tour_booking_details.txtReadMoreIn_Ex
import kotlinx.android.synthetic.main.tour_booking_details.txtTourName
import kotlinx.android.synthetic.main.tour_booking_details.txtcost
import kotlinx.android.synthetic.main.tour_booking_details.txthotel
import kotlinx.android.synthetic.main.tour_booking_details.txtin_ex
import kotlinx.android.synthetic.main.tour_booking_details.txtitinerary
import kotlinx.android.synthetic.main.tour_booking_details.txtoverview
import kotlinx.android.synthetic.main.tour_booking_details.txtratetype
import kotlinx.android.synthetic.main.tour_booking_details.vExclusions
import kotlinx.android.synthetic.main.tour_booking_details.vInclusions
import kotlinx.android.synthetic.main.tour_booking_details.vcost
import kotlinx.android.synthetic.main.tour_booking_details.vhotel
import kotlinx.android.synthetic.main.tour_booking_details.viewPager
import kotlinx.android.synthetic.main.tour_booking_details.vin_ex
import kotlinx.android.synthetic.main.tour_booking_details.vitinerary
import kotlinx.android.synthetic.main.tour_booking_details.voverview
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TourBookingDetailsActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    private val repo by lazy { NetworkRepo() }

    var tourID: Int = 0

    var arrTourDetail: ArrayList<TourBookingDetailsModel> = ArrayList()
    var airlineVoucher: ArrayList<FlightVoucherModel> = ArrayList()
    var hotelVoucher: ArrayList<UpcomingHotelModel> = ArrayList()
    var routeVoucher: ArrayList<RouteVoucherModel> = ArrayList()

    var str_Inclusion : String = ""
    var str_Exclusion : String = ""

    var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tour_booking_details)

        tourID = intent.getIntExtra("TourBookingID",0)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener(this)

        if (isOnline(this)) {
            GetTourBookingDetailsAPI()
        } else {
            toast(getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
        }

        txtReadMore.setOnClickListener {
            if(txtOverViewLess.isVisible() && txtReadMore.text == "Read More") {
                txtOverViewLess.gone()
                txtOverViewMore.visible()
                txtReadMore.text = "Read Less"
                txtReadMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_up, 0);

            } else {
                txtOverViewLess.visible()
                txtOverViewMore.gone()
                txtReadMore.text = "Read More"
                txtReadMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_down, 0);
            }
        }
        txtReadMoreHighlights.setOnClickListener {
            if(txtHighlights.isVisible() && txtReadMoreHighlights.text == "Read More") {
                txtHighlights.gone()
                txtHighlightsMore.visible()
                txtReadMoreHighlights.text = "Read Less"
                txtReadMoreHighlights.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_up, 0);

            } else {
                txtHighlights.visible()
                txtHighlightsMore.gone()
                txtReadMoreHighlights.text = "Read More"
                txtReadMoreHighlights.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_down, 0);
            }
        }
        txtReadMoreIn_Ex.setOnClickListener {
            if(txtOverViewLessIn_Ex.isVisible() && txtReadMoreIn_Ex.text == "Read More") {
                txtOverViewLessIn_Ex.gone()
                txtOverViewMoreIn_Ex.visible()
                txtReadMoreIn_Ex.text = "Read Less"
                txtReadMoreIn_Ex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_up, 0);

            } else {
                txtOverViewLessIn_Ex.visible()
                txtOverViewMoreIn_Ex.gone()
                txtReadMoreIn_Ex.text = "Read More"
                txtReadMoreIn_Ex.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_down, 0);
            }
        }

        txtoverview.setOnClickListener {
            ns.smoothScrollTo(0,0)

        }
        txtitinerary.setOnClickListener {
            ns.smoothScrollTo(0, LL_ITINERARY.top)
        }
        txthotel.setOnClickListener {
            ns.smoothScrollTo(0, LL_HOTEL.top)
        }
        txtcost.setOnClickListener {
            ns.smoothScrollTo(0, LL_COST.top)
        }
        txtin_ex.setOnClickListener {
            ns.smoothScrollTo(0, LL_INEX.top)
        }
        txthotel_voucher.setOnClickListener {
            ns.smoothScrollTo(0, LL_HOTEL_VOUCHER.top)
        }
        txtair_voucher.setOnClickListener {
            ns.smoothScrollTo(0, LL_AIR_VOUCHER.top)
        }
        txtroute_voucher.setOnClickListener {
            ns.smoothScrollTo(0, LL_ROUTE_VOUCHER.top)
        }
        txtInclusions.setOnClickListener {
            txtInclusions.setTextColor(resources.getColor(R.color.colorAccent))
            val typeface = ResourcesCompat.getFont(this, R.font.poppins_semibold)
            txtInclusions.setTypeface(typeface)
            vInclusions.setBackgroundColor(resources.getColor(R.color.colorAccent))

            txtExclusions.setTextColor(resources.getColor(R.color.colorBlack))
            val typeface2 = ResourcesCompat.getFont(this, R.font.poppins_medium)
            txtExclusions.setTypeface(typeface2)
            vExclusions.setBackgroundColor(resources.getColor(R.color.colorTransparent))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtOverViewLessIn_Ex.setText(Html.fromHtml(str_Inclusion, Html.FROM_HTML_MODE_COMPACT))
                txtOverViewMoreIn_Ex.setText(Html.fromHtml(str_Inclusion, Html.FROM_HTML_MODE_COMPACT))
            } else {
                txtOverViewLessIn_Ex.setText(Html.fromHtml(str_Inclusion))
                txtOverViewMoreIn_Ex.setText(Html.fromHtml(str_Inclusion))
            }
        }
        txtExclusions.setOnClickListener {
            txtInclusions.setTextColor(resources.getColor(R.color.colorBlack))
            val typeface2 = ResourcesCompat.getFont(this, R.font.poppins_medium)
            txtInclusions.setTypeface(typeface2)
            vInclusions.setBackgroundColor(resources.getColor(R.color.colorTransparent))

            txtExclusions.setTextColor(resources.getColor(R.color.colorAccent))
            val typeface = ResourcesCompat.getFont(this, R.font.poppins_semibold)
            txtExclusions.setTypeface(typeface)
            vExclusions.setBackgroundColor(resources.getColor(R.color.colorAccent))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtOverViewLessIn_Ex.setText(Html.fromHtml(str_Exclusion, Html.FROM_HTML_MODE_COMPACT))
                txtOverViewMoreIn_Ex.setText(Html.fromHtml(str_Exclusion, Html.FROM_HTML_MODE_COMPACT))

            } else {
                txtOverViewLessIn_Ex.setText(Html.fromHtml(str_Exclusion))
                txtOverViewMoreIn_Ex.setText(Html.fromHtml(str_Exclusion))
            }
        }

        ns.viewTreeObserver.addOnScrollChangedListener {

            val scrollBounds = Rect()
            ns.getHitRect(scrollBounds)

            if (LL_ROUTE_VOUCHER.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorAccent))
            }
            else if (LL_OVERVIEW.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                voverview.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
            else if (LL_AIR_VOUCHER.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vair_voucher.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
            else if (LL_HOTEL_VOUCHER.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
            else  if (LL_INEX.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
            /*else if (LL_HOTEL.getLocalVisibleRect(scrollBounds)) {
             txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
           voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

           txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
           vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

           txthotel.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
           vhotel.setBackgroundColor(resources.getColor(R.color.colorAccent))

           txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
           vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

           txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
           vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

           txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
           vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

           txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
           vair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

           txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
           vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
       }*/
            else if (LL_COST.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vcost.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
            else if (LL_ITINERARY.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txthotel.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txthotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vhotel_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vair_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vroute_voucher.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
        }
    }

    private fun GetTourBookingDetailsAPI() {
        launchProgress()

        repo.getTourBookingDetailsList(tourID,  listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrTourDetail.clear()
                    arrTourDetail = it.data?.Data!!
                    setData(arrTourDetail)
                    disposeProgress()
                } else if (it.data?.Status  == 1010 ||  it.data?.Status  == 201) {
                    arrTourDetail?.clear()
                    disposeProgress()
                }
            } else {
                it.message.toast(this)
                disposeProgress()
            }
        })
    }

    private fun setData(model: ArrayList<TourBookingDetailsModel>) {

        /* AI005 Set Tour Images In Slidong Tab */
        if(model[0].TourImages != null) {
            val TourImagesP = model[0].TourImages
            if(TourImagesP!!.size > 0) {
                val adapter = TourImagePagerAdapter(this, TourImagesP!!)
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

        if(model[0].TourName != null && model[0].TourName != "") {
            txtTourName.text = model[0].TourName.toString()
        }
        if(model[0].NoOfDays != null && model[0].NoOfDays != 0) {
            txtDays.text = model[0].NoOfDays.toString()
        }
        if(model[0].NoOfNights != null && model[0].NoOfNights != 0) {
            txtNights.text = model[0].NoOfNights.toString()
        }
        /* AI005 Set RateTypes */
        if(model[0].RateType != null && model[0].RateType != "") {
            txtratetype.text = model[0].RateType.toString()
        }

        /* AI005 Set TourDate */
        if(model[0].TourDate != null && model[0].TourDate != "") {
            edtDate.setText(model[0].TourDate)
        }

        if(model[0].RoomType != null && model[0].RoomType != "") {
            edtHotelType.setText(model[0].RoomType)
        }

        /* AI005 Set Highlights */
        if(model[0].Highlights != null && model[0].Highlights != "") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtHighlights.text = (Html.fromHtml(model[0].Highlights, Html.FROM_HTML_MODE_COMPACT))
                txtHighlightsMore.text = (Html.fromHtml(model[0].Highlights, Html.FROM_HTML_MODE_COMPACT))
            } else {
                txtHighlights.text = (Html.fromHtml(model[0].Highlights))
                txtHighlightsMore.text = (Html.fromHtml(model[0].Highlights))
            }
            linear_highlights.visible()
        } else {
            linear_highlights.gone()
        }
        /* AI005 Set Overview */
        if(model[0].OverallInclusions != null && model[0].OverallInclusions != "") {
            txtOverViewLess.text = model[0].OverallInclusions.toString()
            txtOverViewMore.text = model[0].OverallInclusions.toString()
            linear_overview.visible()
        } else {
            linear_overview.gone()
        }

        if(model[0].Exclusions != null && model[0].Exclusions != "") {
            str_Exclusion = model[0].Exclusions!!
        }
        if(model[0].Inclusions != null && model[0].Inclusions != "") {
            str_Inclusion = model[0].Inclusions!!

            txtInclusions.setTextColor(resources.getColor(R.color.colorAccent))
            val typeface = ResourcesCompat.getFont(this, R.font.poppins_semibold)
            txtInclusions.setTypeface(typeface)
            vInclusions.setBackgroundColor(resources.getColor(R.color.colorAccent))

            txtExclusions.setTextColor(resources.getColor(R.color.colorBlack))
            val typeface2 = ResourcesCompat.getFont(this, R.font.poppins_medium)
            txtExclusions.setTypeface(typeface2)
            vExclusions.setBackgroundColor(resources.getColor(R.color.colorTransparent))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtOverViewLessIn_Ex.setText(Html.fromHtml(str_Inclusion, Html.FROM_HTML_MODE_COMPACT))
                txtOverViewMoreIn_Ex.setText(Html.fromHtml(str_Inclusion, Html.FROM_HTML_MODE_COMPACT))
            } else {
                txtOverViewLessIn_Ex.setText(Html.fromHtml(str_Inclusion))
                txtOverViewMoreIn_Ex.setText(Html.fromHtml(str_Inclusion))
            }
        }

        /* AI005 Set Tour Facility */
        if(model[0].toufacility != null) {
            val FacilityP = model[0].toufacility
            if(FacilityP != null) {
                if(FacilityP!!.size > 0) {
                    val adapterFacility = FacilityAdapter(this, FacilityP!!)
                    rvFacilityData.adapter  = adapterFacility
                }
                linear_price_of_inclusive.visible()
            } else {
                linear_price_of_inclusive.gone()
            }
        } else {
            linear_price_of_inclusive.gone()
        }

        if(model[0].TotalCost != null && model[0].TotalCost != "") {
            val indiaLocale = Locale("en", "IN")
            val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
            val amount = india.format(model[0].TotalCost!!.toBigDecimal())
            txtRate.text = "" + amount + " /-"
        }
        if(model[0].touritinerary != null) {
            val touritinerary = model[0].touritinerary
            if(touritinerary!!.size > 0) {
                val adapterItinerary= ItineraryAdapter(this, touritinerary!!)
                rvItineraryData.adapter  = adapterItinerary
            }
        }

        if(model[0].tourHotels != null) {
            val tourHotels = model[0].tourHotels
            if(tourHotels != null) {
                if(tourHotels!!.size > 0) {
                    val adapterHotels= HotelAdapter(this, tourHotels!!)
                    rvHotelData.adapter  = adapterHotels
                }
            }
        }

        if(model[0].tourhotelcost != null) {
            val tourhotelcost = model[0].tourhotelcost
            if(tourhotelcost != null) {
                if(tourhotelcost!!.size > 0) {
                    val adapterhotelcost= HotelCostAdapter(this, tourhotelcost!!)
                    rvHotelCostData.adapter  = adapterhotelcost
                }
            }
        }

        var selectedcity = ""
        val citiesName1: ArrayList<String> = ArrayList()
        val citiesP = model[0].touCities
        if(citiesP != null) {
            for(i in 0 until citiesP.size) {
                citiesName1!!.add(citiesP[i].CityName!!+ "(${citiesP[i].NoOfNights!!}N)")
            }
            selectedcity = TextUtils.join(" -> ", citiesName1)
            txtCities.text = selectedcity

        }

        if(model[0].airlineVoucher != null) {
            airlineVoucher.clear()
            airlineVoucher = model[0].airlineVoucher!!
            if(airlineVoucher!!.size > 0) {
                val adapterairline= FlightVoucherListAdapter(this, airlineVoucher!!, this)
                rvAirLineData.adapter  = adapterairline
            }
        }

        if(model[0].hotelVoucher != null) {
            hotelVoucher.clear()
            hotelVoucher = model[0].hotelVoucher!!
            if(hotelVoucher!!.size > 0) {
                val adapterHotels= UpcomingHotelListAdapter(this, hotelVoucher!!, this, false)
                rvHotelVoucherData.adapter  = adapterHotels
            }
        }

        if(model[0].routeVoucher != null) {
            routeVoucher.clear()
            routeVoucher = model[0].routeVoucher!!
            if(routeVoucher!!.size > 0) {
                val adapterRoute= RouteVoucherListAdapter(this, routeVoucher!!, this)
                rvRouteVoucherData.adapter  = adapterRoute
            }
        }
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when(type) {
            108 -> {
                val intent = Intent(this, FlightVoucherDetailActivity::class.java)
                intent.putExtra("ID",airlineVoucher[position].ID)
                startActivity(intent)
            }
            107 -> {
                val intent = Intent(this, HotelVoucherDetailsActivity::class.java)
//                intent.putExtra("HotelID",hotelVoucher[position].HotelID)
//                intent.putExtra("TourID",hotelVoucher[position].TourID)
//                intent.putExtra("VoucherNo",hotelVoucher[position].HotelVoucher)
                intent.putExtra("HotelVoucherID",hotelVoucher[position].HotelVoucherID)

                startActivity(intent)
            }
            109 -> {
                val intent = Intent(this, RouteVoucherDetailsActivity::class.java)
                intent.putExtra("ID",routeVoucher[position].ID)
//                intent.putExtra("TourBookingNo",routeVoucher[position].TourBookingNo)
                startActivity(intent)
            }
        }

    }

}