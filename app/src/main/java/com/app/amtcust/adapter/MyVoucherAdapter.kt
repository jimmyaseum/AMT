package com.app.amtcust.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.amtcust.fragment.voucher.FlightFragment
import com.app.amtcust.fragment.voucher.HotelFragment
import com.app.amtcust.fragment.voucher.RouteFragment


class MyVoucherAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                val homeFragment = HotelFragment()
                return homeFragment
            }
            1 -> {
                val flightFragment = FlightFragment()
                return flightFragment
            }
            2 -> {
                val routeFragment = RouteFragment()
                return routeFragment
            }
            else -> {
                val homeFragment = HotelFragment()
                return homeFragment
            }
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}