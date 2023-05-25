package com.app.amtcust.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app.amtcust.R
import com.app.amtcust.adapter.RecentCompeleteListAdapter
import com.app.amtcust.adapter.UpcomingTourListAdapter
import com.app.amtcust.model.response.TourBookingModel
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_my_trip.*
import kotlinx.android.synthetic.main.layout_no_data.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MyTripListActivity : BaseActivity(), View.OnClickListener {

    var sharedPreference: SharedPreference? = null

    private val repo by lazy { NetworkRepo() }

    private var arrongoingList: ArrayList<TourBookingModel> = ArrayList()
    private var arrrecentList: ArrayList<TourBookingModel> = ArrayList()
    private var arrupcomingList: ArrayList<TourBookingModel> = ArrayList()

    var TourBooking : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trip)
        initializeView()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        initListner()
    }

    private fun initListner() {

        imgBack.setOnClickListener(this)
        card_Add_Update.setOnClickListener(this)
        card_View.setOnClickListener(this)

        if (isOnline(this)) {
            GetongoingTripList(1)
        } else {
            toast(
                getString(R.string.str_msg_no_internet),
                AppConstant.TOAST_SHORT
            )
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.card_Add_Update -> {
                val intent = Intent(this, TourBookingFormActivity::class.java)
                intent.putExtra("ID",TourBooking)
                startActivity(intent)
            }
            R.id.card_View -> {
                val intent = Intent(this, TourBookingDetailsActivity::class.java)
                intent.putExtra("TourBookingID",TourBooking)
                startActivity(intent)
            }
        }
    }

    private fun GetongoingTripList(opty: Int) {

        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getTripList(userId, opty, listners = ResponseListner {
            if (it.status) {
                if (it.data?.Status == 200) {

                    val arrList = it.data?.Data!!

                    arrongoingList.clear()
                    arrongoingList = arrList.ongoingTrips!!

                    if(arrongoingList.size > 0) {
                        TourBooking = arrongoingList[0].ID!!

                        if (arrongoingList[0].TourName != null) {
                            txtTourName.text = arrongoingList[0].TourName
                        }
                        if (arrongoingList[0].TotalCost != null) {

                            val indiaLocale = Locale("en", "IN")
                            val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
                            val amount = india.format(arrongoingList[0].TotalCost!!.toBigDecimal())

                            txtTourCost.text = amount + "/-"
                        }
                        if (arrongoingList[0].TourImage != null) {
                            img.loadUrlRoundedCorner(
                                arrongoingList[0].TourImage,
                                R.drawable.ic_image,
                                1
                            )
                        }
                        if (arrongoingList[0].TourStartDate != null) {
                            val calendar1: Calendar = Calendar.getInstance()
                            calendar1.add(Calendar.DATE, arrongoingList[0].TourStartDate!!.toInt())
                            val startdate = convertDateToString(calendar1.time, AppConstant.dd_MM_yyyy_Slash)

                            val calendar2: Calendar = Calendar.getInstance()
                            val returnDate =
                                convertDateToString(calendar2.time, AppConstant.dd_MM_yyyy_Slash)

                            var days = getFormattedMonth(
                                startdate,
                                returnDate,
                                AppConstant.dd_MM_yyyy_Slash
                            )

                            if (days.contains(",")) {
                                var split = days.split(",")
                                Log.e("split[0]", "-->" + split[0])
                                Log.e("split[1]", "-->" + split[1])
                            }

                            txtDaysago.text = days + " ago and "
                        }
                        if (arrongoingList[0].TourEndDate != null) {
                            val calendar1: Calendar = Calendar.getInstance()
                            val startdate =
                                convertDateToString(calendar1.time, AppConstant.dd_MM_yyyy_Slash)

                            val calendar2: Calendar = Calendar.getInstance()
                            calendar2.add(Calendar.DATE, arrongoingList[0].TourEndDate!!.toInt())
                            val returnDate =
                                convertDateToString(calendar2.time, AppConstant.dd_MM_yyyy_Slash)

                            var days = getFormattedMonth(
                                startdate,
                                returnDate,
                                AppConstant.dd_MM_yyyy_Slash
                            )

                            if (days.contains(",")) {
                                var split = days.split(",")
                                Log.e("split[0]", "-->" + split[0])
                                Log.e("split[1]", "-->" + split[1])
                            }

                            txtDaystogo.text = days + " to go"
                        }
                        if (arrongoingList[0].customers != null) {
                            val customersP = arrongoingList[0].customers
                            if (customersP != null) {
                                if (customersP.size != 0) {
                                    if (customersP.size == 1) {
                                        profile_image1.loadUrlRoundedCorner(
                                            customersP[0].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        profile_image2.gone()
                                        profile_image3.gone()
                                        txtMoreFamilymember.gone()
                                    } else if (customersP.size == 2) {
                                        profile_image1.loadUrlRoundedCorner(
                                            customersP[0].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        profile_image2.loadUrlRoundedCorner(
                                            customersP[1].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        profile_image3.gone()
                                        txtMoreFamilymember.gone()
                                    } else if (customersP.size == 3) {
                                        profile_image1.loadUrlRoundedCorner(
                                            customersP[0].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        profile_image2.loadUrlRoundedCorner(
                                            customersP[1].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        profile_image2.loadUrlRoundedCorner(
                                            customersP[2].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        txtMoreFamilymember.gone()
                                    } else {
                                        profile_image1.loadUrlRoundedCorner(
                                            customersP[0].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        profile_image2.loadUrlRoundedCorner(
                                            customersP[1].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )
                                        profile_image3.loadUrlRoundedCorner(
                                            customersP[2].CustomerImage,
                                            R.drawable.ic_image,
                                            1
                                        )

                                        val more = customersP.size - 3
                                        txtMoreFamilymember.text = "+ " + more + " more"
                                        txtMoreFamilymember.visible()
                                    }
                                } else {
                                    profile_image1.gone()
                                    profile_image2.gone()
                                    profile_image3.gone()
                                    txtMoreFamilymember.gone()
                                }
                            } else {
                                profile_image1.gone()
                                profile_image2.gone()
                                profile_image3.gone()
                                txtMoreFamilymember.gone()
                            }
                        }
                        LL_OnGoing.visible()
                        NSV.visible()
                        rlMainNoData.gone()
                    } else {
                        LL_OnGoing.gone()
                    }

                    arrrecentList.clear()
                    arrrecentList = arrList.completedTrip!!
                    if(arrrecentList.size > 0) {
                        rvRecentlyCompleted.adapter = RecentCompeleteListAdapter(this,arrrecentList)
                        LL_Recent_Complete.visible()
                        NSV.visible()
                        rlMainNoData.gone()
                    } else {
                        LL_Recent_Complete.gone()
                    }

                    arrupcomingList.clear()
                    arrupcomingList = arrList.upcomingTrips!!
                    if(arrupcomingList.size > 0) {
                        rvUpcomingTours.adapter = UpcomingTourListAdapter(this,arrupcomingList)

                        LL_UpComing.visible()
                        NSV.visible()
                        rlMainNoData.gone()
                    } else {
                        LL_UpComing.gone()
                    }

                    if(arrongoingList.size == 0 && arrrecentList.size == 0 && arrupcomingList.size == 0) {
                        NSV.gone()
                        rlMainNoData.visible()
                        tvNoData.text = "No Data Found"
                        imgNoData.setImageResource(R.drawable.tour_icon)
                        tvRetry.gone()
                    }
                } else if (it.data?.Status  == 1010 ||  it.data?.Status  == 201) {
                    toast(it.data?.Details.toString(), AppConstant.TOAST_SHORT)
                    LL_OnGoing.gone()
                    LL_Recent_Complete.gone()
                    LL_UpComing.gone()
                }
            } else {
                it.message.toast(this)
                LL_OnGoing.gone()
                LL_Recent_Complete.gone()
                LL_UpComing.gone()
            }
        })
    }

}