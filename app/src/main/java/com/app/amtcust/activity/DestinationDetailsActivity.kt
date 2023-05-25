package com.app.amtcust.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.FacilityAdapter
import com.app.amtcust.adapter.Filter.DateAdapter
import com.app.amtcust.adapter.Filter.MonthAdapter
import com.app.amtcust.adapter.HotelCostAdapter
import com.app.amtcust.adapter.ItineraryAdapter
import com.app.amtcust.adapter.TourImagePagerAdapter
import com.app.amtcust.adapter.dialog.*
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.*
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DestinationDetailsActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    private val repo by lazy { NetworkRepo() }

    var tourID: Int = 0
    var RateType: String = ""
    var NoOfNights: Int = 0
    var RoomTypeID: Int = 0

    var arrTourDetail: ArrayList<TourDetailsModel> = ArrayList()

    var arrRoomType: ArrayList<HotelRoom>? = null
//    var RoomTypeId : Int = 0
    var RoomTypeName : String = ""

    var arrDates: ArrayList<TourDates>? = null
    var SelectedDate : String = ""

    var str_Inclusion : String = ""
    var str_Exclusion : String = ""

    var timer: Timer? = null
    var sharedPreference: SharedPreference? = null

    private var arrMonthList: ArrayList<MonthDataModel> = ArrayList()
    private lateinit var monthadapter: MonthAdapter

    private var arrDateList: ArrayList<DateDataModel> = ArrayList()
    private lateinit var dateadapter: DateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        tourID = intent.getIntExtra("TourID",0)
        NoOfNights = intent.getIntExtra("NoOfNights",0)
        RateType = intent.getStringExtra("RateType").toString()
        RoomTypeID = intent.getIntExtra("RoomTypeID",0)
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener(this)
        edtHotelType.setOnClickListener(this)
        edtDate.setOnClickListener(this)
        txtInquiryNow.setOnClickListener(this)
        txtCall_Us.setOnClickListener(this)

        monthadapter = MonthAdapter(this@DestinationDetailsActivity, arrMonthList, this)
        rvMonth.adapter = monthadapter

        dateadapter = DateAdapter(this@DestinationDetailsActivity, arrDateList)
        rvDate.adapter = dateadapter

        if (isOnline(this)) {
            GetTourDetailsAPI()
            callTourMonthApi(tourID)
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
        txtcost.setOnClickListener {
            ns.smoothScrollTo(0, LL_COST.top)
        }
        txtin_ex.setOnClickListener {
            ns.smoothScrollTo(0, LL_INEX.top)
        }

        txtInclusions.setOnClickListener {
            txtInclusions.setTextColor(resources.getColor(R.color.colorAccent))
            val typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
            txtInclusions.setTypeface(typeface)
            vInclusions.setBackgroundColor(resources.getColor(R.color.colorAccent))

            txtExclusions.setTextColor(resources.getColor(R.color.colorBlack))
            val typeface2 = ResourcesCompat.getFont(this, R.font.poppins_semibold)
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
            val typeface2 = ResourcesCompat.getFont(this, R.font.poppins_semibold)
            txtInclusions.setTypeface(typeface2)
            vInclusions.setBackgroundColor(resources.getColor(R.color.colorTransparent))

            txtExclusions.setTextColor(resources.getColor(R.color.colorAccent))
            val typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
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

            if (LL_INEX.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorAccent))
            }
            else if (LL_OVERVIEW.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                voverview.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
            else if (LL_COST.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vcost.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

            }
            else if (LL_ITINERARY.getLocalVisibleRect(scrollBounds)) {
                txtoverview.setBackgroundColor(resources.getColor(R.color.colorWhite))
                voverview.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtitinerary.setBackgroundColor(resources.getColor(R.color.colorLightBg1))
                vitinerary.setBackgroundColor(resources.getColor(R.color.colorAccent))

                txtcost.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vcost.setBackgroundColor(resources.getColor(R.color.colorWhite))

                txtin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))
                vin_ex.setBackgroundColor(resources.getColor(R.color.colorWhite))

            }
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.edtHotelType -> {
                if(!arrRoomType.isNullOrEmpty()) {
                    selectRoomTypeDialog()
                } else {
                    toast("No Room Type Available",Toast.LENGTH_SHORT)
                }
            }
            R.id.edtDate -> {
                if(!arrDates.isNullOrEmpty()) {
                    selectDateDialog()
                } else {
                    toast("No Dates Available",Toast.LENGTH_SHORT)
                }
            }
            R.id.txtInquiryNow -> {
                selectDialog()
            }
            R.id.txtCall_Us -> {
                sharedPreference = SharedPreference(this)
                val MobileNum = sharedPreference?.getPreferenceString(PrefConstants.PREF_ADMIN_CALL)

                if (!MobileNum.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${MobileNum}")
                    startActivity(intent)

                }
            }
        }
    }

    @SuppressLint("Range")
    private fun GetTourDetailsAPI() {
        launchProgress()

        repo.getTourDetailsList(tourID,RateType,NoOfNights,RoomTypeID,  listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrTourDetail.clear()
                    arrTourDetail = it.data?.Data!!
                    setData(arrTourDetail)

                    Handler().postDelayed({
                        annonce_main_coordinator.animate().alpha(5.0f)
                        disposeProgress()
                    }, 2000)

                } else if (it.data?.Status  == 1010 ||  it.data?.Status  == 201) {
                    arrTourDetail?.clear()
                    it.message.toast(this)
                    disposeProgress()
                }
            } else {
                it.message.toast(this)
                disposeProgress()
            }
        })
    }

    private fun setData(model: ArrayList<TourDetailsModel>) {

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
        if(model[0].RateType != null && model[0].RateType != "") {
            txtratetype.text = model[0].RateType.toString()
        }

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
            val typeface = ResourcesCompat.getFont(this, R.font.poppins_bold)
            txtInclusions.setTypeface(typeface)
            vInclusions.setBackgroundColor(resources.getColor(R.color.colorAccent))

            txtExclusions.setTextColor(resources.getColor(R.color.colorBlack))
            val typeface2 = ResourcesCompat.getFont(this, R.font.poppins_semibold)
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

//        if(model[0].Toudate != null) {
//            arrDates = model[0].Toudate
//        }

        if(model[0].touritinerary != null) {
            val touritinerary = model[0].touritinerary
            if(touritinerary!!.size > 0) {
                val adapterItinerary= ItineraryAdapter(this, touritinerary!!)
                rvItineraryData.adapter  = adapterItinerary
            }

        }
        if(model[0].tourcost != null) {
            arrRoomType = model[0].tourcost
            for(i in 0 until arrRoomType!!.size) {
                if(arrRoomType!![i].RoomType == "DELUXE") {

//                    RoomTypeId = arrRoomType!![i].RoomTypeID!!
                    RoomTypeName = arrRoomType!![i].RoomType!!
                    edtHotelType.setText(arrRoomType!![i].RoomType)

                    if(arrRoomType!![i].Rate!!.toDouble() != 0.0) {
                        val indiaLocale = Locale("en", "IN")
                        val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
                        val amount = india.format(arrRoomType!![i].Rate!!.toBigDecimal())
                        txtRate.text = amount + " /-"
                        txtRate.visible()
                    } else {
                        txtRate.gone()
                    }
                } else {

//                    RoomTypeId = arrRoomType!![0].RoomTypeID!!
                    RoomTypeName = arrRoomType!![0].RoomType!!
                    edtHotelType.setText(arrRoomType!![0].RoomType)
                    if(arrRoomType!![0].Rate!!.toDouble() != 0.0) {
                        val indiaLocale = Locale("en", "IN")
                        val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
                        val amount = india.format(arrRoomType!![0].Rate!!.toBigDecimal())
                        txtRate.text = amount + " /-"
                        txtRate.visible()
                    } else {
                        txtRate.gone()
                    }
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
    }

    /** AI005
     * This method is to open Room Type dialog
     */
    private fun selectRoomTypeDialog() {
        var dialogSelectRoomType = Dialog(this)
        dialogSelectRoomType.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_room_type, null)
        dialogSelectRoomType.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectRoomType.window!!.attributes)

        dialogSelectRoomType.window!!.attributes = lp
        dialogSelectRoomType.setCancelable(true)
        dialogSelectRoomType.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectRoomType.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectRoomType.window!!.setGravity(Gravity.CENTER)

        val rvRoomTypeList = dialogSelectRoomType.findViewById(R.id.rvRoomTypeList) as RecyclerView

        val itemAdapter = DialogRoomTypeAdapter(RoomTypeName,this, arrRoomType!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                itemAdapter.updateItemSingle(pos)
//                RoomTypeId = arrRoomType!![pos].RoomTypeID!!
                RoomTypeName = arrRoomType!![pos].RoomType!!
                edtHotelType.setText(arrRoomType!![pos].RoomType)

                if(arrRoomType!![pos].Rate!!.toDouble() != 0.0) {
                    val indiaLocale = Locale("en", "IN")
                    val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
                    val amount = india.format(arrRoomType!![pos].Rate!!.toBigDecimal())

                    txtRate.text = amount + " /-"
                    txtRate.visible()
                } else {
                    txtRate.gone()
                }

                dialogSelectRoomType!!.dismiss()
            }
        })
        rvRoomTypeList.adapter = itemAdapter
        dialogSelectRoomType!!.show()
    }

    /** AI005
     * This method is to open date dialog
     */
    private fun selectDateDialog() {
        var dialogSelectdate = Dialog(this)
        dialogSelectdate.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_room_type, null)
        dialogSelectdate.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectdate.window!!.attributes)

        dialogSelectdate.window!!.attributes = lp
        dialogSelectdate.setCancelable(true)
        dialogSelectdate.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectdate.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectdate.window!!.setGravity(Gravity.CENTER)

        val rvRoomTypeList = dialogSelectdate.findViewById(R.id.rvRoomTypeList) as RecyclerView

        val itemAdapter = DialogDateAdapter(this, arrDates!!)
        itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
            override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                itemAdapter.updateItemSingle(pos)
                SelectedDate = arrDates!![pos].TourDate!!
                edtDate.setText(arrDates!![pos].TourDate)
                dialogSelectdate!!.dismiss()
            }
        })
        rvRoomTypeList.adapter = itemAdapter
        dialogSelectdate!!.show()
    }

    private fun selectDialog() {

        val arrTypeList: ArrayList<String> = ArrayList()
        var arrSectorList: ArrayList<SectorListModel> = ArrayList()

        val repo by lazy { NetworkRepo() }

        val dialogMember = Dialog(this)
        dialogMember.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_inquiry_now, null)
        dialogMember.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogMember.window!!.attributes)

        dialogMember.window!!.attributes = lp
        dialogMember.setCancelable(true)
        dialogMember.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMember.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogMember.window!!.setGravity(Gravity.CENTER)

        val etName = dialogMember.findViewById(R.id.etName) as EditText
        val etEmail = dialogMember.findViewById(R.id.etEmail) as EditText
        val etMobile = dialogMember.findViewById(R.id.etMobile) as EditText
        val etType = dialogMember.findViewById(R.id.etType) as EditText
        val etDestination = dialogMember.findViewById(R.id.etDestination) as EditText

        val ImgClose = dialogMember.findViewById(R.id.ImgClose) as ImageView
        val CardCancel = dialogMember.findViewById(R.id.CardCancel) as CardView
        val CardInquiryNow = dialogMember.findViewById(R.id.CardInquiryNow) as CardView

        // AI005 Mr, Mrs, Ms, Dr
        arrTypeList.add("Domestic")
        arrTypeList.add("International")

        ImgClose.setOnClickListener {

            dialogMember.dismiss()
        }
        CardCancel.setOnClickListener {

            dialogMember.dismiss()
        }
        CardInquiryNow.setOnClickListener {
            showProgress()

            val jsonObject = JSONObject()
            jsonObject.put("Name",etName.text.toString().trim())
            jsonObject.put("Destination",etDestination.text.toString().trim())
            jsonObject.put("MobileNo", etMobile.text.toString().trim())
            jsonObject.put("EmailID", etEmail.text.toString().trim())
            jsonObject.put("TourType", etType.text.toString().trim())
            jsonObject.put("EmployeeID", 0)
            jsonObject.put("IsActive", true)

            val call = ApiUtils.apiInterface.CreateInquiry(getRequestJSONBody(jsonObject.toString()))

            call.enqueue(object : Callback<CommonResponse> {
                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                    if (response.code() == 200) {

                        if (response.body()?.code == 200) {
                            dialogMember.dismiss()
                            toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                        } else {
                            hideProgress()
                            dialogMember.dismiss()
                            toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                        }
                    }
                }
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    hideProgress()
                    dialogMember.dismiss()
                    toast(resources.getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
                }
            })
        }

        etDestination.setOnClickListener {
            if(arrSectorList.size > 0) {
                var dialogSelectCity = Dialog(this)
                dialogSelectCity.requestWindowFeature(Window.FEATURE_NO_TITLE)

                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select, null)
                dialogSelectCity.setContentView(dialogView)

                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialogSelectCity.window!!.attributes)

                dialogSelectCity.window!!.attributes = lp
                dialogSelectCity.setCancelable(true)
                dialogSelectCity.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                dialogSelectCity.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                dialogSelectCity.window!!.setGravity(Gravity.CENTER)

                val rvDialogCustomer = dialogSelectCity.findViewById(R.id.rvDialogCustomer) as RecyclerView
                val edtSearchCustomer = dialogSelectCity.findViewById(R.id.edtSearchCustomer) as EditText

                val itemAdapter = DialogSectorAdapter(this, arrSectorList!!)
                itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                    override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                        etDestination.setText(arrSectorList[pos].SectorName)
                        dialogSelectCity!!.dismiss()
                    }
                })

                rvDialogCustomer.adapter = itemAdapter

                edtSearchCustomer.gone()
                dialogSelectCity!!.show()
            } else {
                var jsonObject = JSONObject()
                jsonObject.put("SectorType", etType.text.toString())

                val call = ApiUtils.apiInterface.getAllSector(getRequestJSONBody(jsonObject.toString()))

                call.enqueue(object : Callback<SectorListResponse> {
                    override fun onResponse(call: Call<SectorListResponse>, response: Response<SectorListResponse>) {

                        if (response.code() == 200) {
                            LogUtil.e(TAG, "=====onResponse====")

                            if (response.body()?.Status == 200) {
                                hideProgress()
                                arrSectorList = response.body()?.Data!!
                                if(arrSectorList!!.size > 0) {
                                    etDestination.performClick()
                                } else {
                                    toast("No Value Available.", AppConstant.TOAST_SHORT)
                                }

                            } else {
                                toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                                hideProgress()
                            }
                        }
                    }

                    override fun onFailure(call: Call<SectorListResponse>, t: Throwable) {
                        hideProgress()
                        toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
                    }
                })
            }
        }

        etType.setText(arrTypeList[0])
        etType.setOnClickListener {
            var dialogSelectInitial = Dialog(this)
            dialogSelectInitial.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select, null)
            dialogSelectInitial.setContentView(dialogView)

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialogSelectInitial.window!!.attributes)

            dialogSelectInitial.window!!.attributes = lp
            dialogSelectInitial.setCancelable(true)
            dialogSelectInitial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogSelectInitial.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialogSelectInitial.window!!.setGravity(Gravity.CENTER)

            val rvDialogCustomer = dialogSelectInitial.findViewById(R.id.rvDialogCustomer) as RecyclerView
            val edtSearchCustomer = dialogSelectInitial.findViewById(R.id.edtSearchCustomer) as EditText

            val itemAdapter = DialogInitialAdapter(this, arrTypeList!!)
            itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                    etType.setText(arrTypeList!![pos])
                    dialogSelectInitial!!.dismiss()
                }

            })

            rvDialogCustomer.adapter = itemAdapter

            edtSearchCustomer.gone()
            dialogSelectInitial!!.show()
        }
        dialogMember!!.show()
    }

    // region Get Tour Detail Api
    private fun callTourMonthApi(tourid: Int) {

        val call = ApiUtils.apiInterface2.getTourDates(tourid)
        call.enqueue(object : Callback<TourMonthResponse> {
            override fun onResponse(call: Call<TourMonthResponse>, response: Response<TourMonthResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                            arrMonthList.clear()
                            arrMonthList = response.body()?.Data!![0].monthData!!

                        Log.e("monthdata size","==>"+arrMonthList.size)
                        if(arrMonthList.size > 0) {
                            arrMonthList[0].isSelected = true

                            monthadapter = MonthAdapter(this@DestinationDetailsActivity, arrMonthList,this@DestinationDetailsActivity)
                            rvMonth.adapter = monthadapter

                            arrDateList = ArrayList()
                            arrDateList = arrMonthList[0].DateData!!

                            if(arrDateList.size > 0) {
                                dateadapter = DateAdapter(this@DestinationDetailsActivity, arrDateList)
                                rvDate.adapter = dateadapter
                                rvDate.visible()
                            } else {
                                rvDate.gone()
                            }

                        }
                    }
                }
            }
            override fun onFailure(call: Call<TourMonthResponse>, t: Throwable) {
            }
        })
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when(type) {
            1 -> {
                val arrDateList = arrMonthList[position].DateData!!

                if(arrDateList.size > 0) {
                    dateadapter = DateAdapter(this@DestinationDetailsActivity, arrDateList)
                    rvDate.adapter = dateadapter
                    rvDate.visible()
                } else {
                    rvDate.gone()
                }

                monthadapter.updateItemSingle(position)
            }
        }
    }

    // endregion

}