package com.app.amtcust.fragment.voucher

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtcust.R
import com.app.amtcust.activity.FlightVoucherDetailActivity
import com.app.amtcust.adapter.voucher.FlightVoucherListAdapter
import com.app.amtcust.fragment.BaseFragment
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.FlightVoucherModel
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.fragment_previous_flight_list.view.*
import kotlinx.android.synthetic.main.layout_no_data.view.*

class FutureFlightFragment : BaseFragment(), RecyclerClickListener {

    var sharedPreference: SharedPreference? = null
    private var views: View? = null
    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: FlightVoucherListAdapter
    private var arrFlightVoucherList: ArrayList<FlightVoucherModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_previous_flight_list, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        if (isOnline(activity!!)) {
            GetFutureList()
        } else {
            showHideDesignView(3)
        }

        views!!.swipeRefreshPreviousFlight.setOnRefreshListener {
            if (isOnline(activity!!)) {
                GetFutureList()
            } else {
                showHideDesignView(3)
            }
        }
        views!!.tvRetry.setOnClickListener {
            if (isOnline(activity!!)) {
                GetFutureList()
            } else {
                showHideDesignView(3)
            }
        }
    }

    private fun GetFutureList() {

        showProgress()
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getPrevious(userId,2, listners = ResponseListner {
            if (it.status) {
                if (it.data?.Status == 200) {
                    arrFlightVoucherList.clear()
                    arrFlightVoucherList = it.data?.Data!!

                    if(arrFlightVoucherList.size > 0) {
                        views!!.rvPreviousFlightList.adapter = FlightVoucherListAdapter(activity,arrFlightVoucherList,this)
                        views!!.swipeRefreshPreviousFlight.isRefreshing = false
                        hideProgress()
                        showHideDesignView(1)
                    }
                } else {
                    views!!.swipeRefreshPreviousFlight.isRefreshing = false
                    hideProgress()
                    showHideDesignView(2)
                }
            } else {
                views!!.swipeRefreshPreviousFlight.isRefreshing = false
                hideProgress()
            }

        })
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        val intent = Intent(activity!!, FlightVoucherDetailActivity::class.java)
        intent.putExtra("ID",arrFlightVoucherList[position].ID)
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
                views!!.rvPreviousFlightList.visible()
                views!!.rlMainNoData.gone()
            }
            2 -> {

                views!!.tvNoData.text = "No Upcoming Flight Voucher Found"
                views!!.imgNoData.setImageResource(R.drawable.flight_voucher)

                views!!.rvPreviousFlightList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.gone()
            }
            3 -> {
                views!!.tvNoData.text = getString(R.string.msg_no_internet)
                views!!.imgNoData.setImageResource(R.drawable.ic_no_internet)

                views!!.rvPreviousFlightList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
            4 -> {
                views!!.tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                views!!.imgNoData.setImageResource(R.drawable.ic_oops)

                views!!.rvPreviousFlightList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
        }
    }
}