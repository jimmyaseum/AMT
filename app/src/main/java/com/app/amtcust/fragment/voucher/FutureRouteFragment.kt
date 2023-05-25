package com.app.amtcust.fragment.voucher

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.activity.RouteVoucherDetailsActivity
import com.app.amtcust.adapter.voucher.RouteVoucherListAdapter
import com.app.amtcust.fragment.BaseFragment
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.RouteVoucherModel
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.fragment_route_list.view.*
import kotlinx.android.synthetic.main.layout_no_data.view.*

class FutureRouteFragment : BaseFragment(), RecyclerClickListener {

    var sharedPreference: SharedPreference? = null
    private var views: View? = null
    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: RouteVoucherListAdapter
    private var arrRouteVoucherList: ArrayList<RouteVoucherModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_route_list, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)

        val layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
        views!!.rvRouteList.layoutManager = layoutManager

        if (isOnline(activity!!)) {
            GetFutureRouteList()
        } else {
            showHideDesignView(3)
        }
        views!!.swipeRefreshRoute.setOnRefreshListener {
            if (isOnline(activity!!)) {
                GetFutureRouteList()
            } else {
                showHideDesignView(3)
            }
        }
        views!!.tvRetry.setOnClickListener {
            if (isOnline(activity!!)) {
                GetFutureRouteList()
            } else {
                showHideDesignView(3)
            }
        }
    }

    private fun GetFutureRouteList() {

        showProgress()
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getFutureRouteList(userId,9, listners = ResponseListner {
            if (it.status) {
                if (it.data?.Status == 200) {
                    arrRouteVoucherList.clear()
                    arrRouteVoucherList = it.data?.Data!!

                    if(arrRouteVoucherList.size > 0) {
                        views!!.rvRouteList.adapter = RouteVoucherListAdapter( activity!! ,arrRouteVoucherList,this)
                        views!!.swipeRefreshRoute.isRefreshing = false
                        hideProgress()
                        showHideDesignView(1)
                    }
                } else {
                    views!!.swipeRefreshRoute.isRefreshing = false
                    hideProgress()
                    showHideDesignView(2)
                }
            } else {
                views!!.swipeRefreshRoute.isRefreshing = false
                hideProgress()
            }

        })
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        val intent = Intent(activity!!, RouteVoucherDetailsActivity::class.java)
        intent.putExtra("ID",arrRouteVoucherList[position].ID)
//        intent.putExtra("TourBookingNo",arrRouteVoucherList[position].TourBookingNo)
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
                views!!.rvRouteList.visible()
                views!!.rlMainNoData.gone()
            }
            2 -> {

                views!!.tvNoData.text = "No Upcoming Route Voucher Found"
                views!!.imgNoData.setImageResource(R.drawable.route_voucher)

                views!!.rvRouteList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.gone()
            }
            3 -> {
                views!!.tvNoData.text = getString(R.string.msg_no_internet)
                views!!.imgNoData.setImageResource(R.drawable.ic_no_internet)

                views!!.rvRouteList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
            4 -> {
                views!!.tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                views!!.imgNoData.setImageResource(R.drawable.ic_oops)

                views!!.rvRouteList.gone()
                views!!.rlMainNoData.visible()
                views!!.tvRetry.visible()
            }
        }
    }
}