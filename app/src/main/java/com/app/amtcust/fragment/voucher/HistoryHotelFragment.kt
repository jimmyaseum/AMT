package com.app.amtcust.fragment.voucher

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtcust.R
import com.app.amtcust.activity.HotelVoucherDetailsActivity
import com.app.amtcust.adapter.voucher.HistoryHotelListAdapter
import com.app.amtcust.fragment.BaseFragment
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.UpcomingHotelModel
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.fragment_upcoming_hotel_list.view.*
import kotlinx.android.synthetic.main.layout_no_data.view.*

class HistoryHotelFragment : BaseFragment(), RecyclerClickListener {
    var sharedPreference: SharedPreference? = null
    private var views: View? = null
    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: HistoryHotelListAdapter
    private var arrUpcomingHotelList: ArrayList<UpcomingHotelModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_upcoming_hotel_list, container, false)
        initializeView()
        return views
    }
    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        if (isOnline(activity!!)) {
            GetUpcomingHotelList()
        } else {
            showHideDesignView(3)
        }

        views!!.swipeRefreshUpcoming.setOnRefreshListener {
            if (isOnline(activity!!)) {
                GetUpcomingHotelList()
            } else {
                showHideDesignView(3)
            }
        }

        views!!.tvRetry.setOnClickListener {
            if (isOnline(activity!!)) {
                GetUpcomingHotelList()
            } else {
                showHideDesignView(3)
            }
        }
    }
    private fun GetUpcomingHotelList() {

        showProgress()
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getUpcomingHotel(userId,4, listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrUpcomingHotelList.clear()
                    arrUpcomingHotelList = it.data?.Data!!

                    if(arrUpcomingHotelList.size > 0) {
                        views!!.rvUpcomingHotelList.adapter = HistoryHotelListAdapter(activity,arrUpcomingHotelList,this)
                        views!!.swipeRefreshUpcoming.isRefreshing = false
                        hideProgress()
                        showHideDesignView(1)
                    }
                } else {
                    views!!.swipeRefreshUpcoming.isRefreshing = false
                    hideProgress()
                    showHideDesignView(2)
                }
            } else {
                views!!.swipeRefreshUpcoming.isRefreshing = false
                hideProgress()
            }

        })
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        val intent = Intent(activity!!, HotelVoucherDetailsActivity::class.java)
//        intent.putExtra("HotelID",arrUpcomingHotelList[position].HotelID)
//        intent.putExtra("TourID",arrUpcomingHotelList[position].TourID)
//        intent.putExtra("VoucherNo",arrUpcomingHotelList[position].HotelVoucher)

        intent.putExtra("HotelVoucherID",arrUpcomingHotelList[position].HotelVoucherID)
        startActivity(intent)
    }

    /*1st Parameter - type
* 1 - RecyclerView visible and others are gone
* 2 - No data found
* 3 - No internet found
* 4 - Oops something went wrong
* */
    private fun showHideDesignView(mode: Int) {

        when (mode) {
            1 -> {
                views!!.rvUpcomingHotelList.visible()
                views!!.rlMainNoData.gone()
            }
            2 -> {

                views!!.tvNoData.text = "No History Found"
                views!!.imgNoData.setImageResource(R.drawable.hotel_voucher)

                views!!.rvUpcomingHotelList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.gone()
            }
            3 -> {
                views!!.tvNoData.text = getString(R.string.msg_no_internet)
                views!!.imgNoData.setImageResource(R.drawable.ic_no_internet)

                views!!.rvUpcomingHotelList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
            4 -> {
                views!!.tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                views!!.imgNoData.setImageResource(R.drawable.ic_oops)

                views!!.rvUpcomingHotelList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
        }
    }
}