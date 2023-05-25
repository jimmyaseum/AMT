package com.app.amtcust.fragment.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtcust.R
import com.app.amtcust.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_hotel.view.*

class HotelFragment : BaseFragment(), View.OnClickListener {

    private var views: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_hotel, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        initListeners()
    }

    private fun initListeners() {
        views!!.txtUpcoming.setOnClickListener(this)
        views!!.txtHistory.setOnClickListener(this)

        val fragment = UpcomingHotelFragment()
        replaceFragment(R.id.container2, fragment, UpcomingHotelFragment::class.java.simpleName)
        UpdateTabUpcoming()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txtUpcoming -> {
                UpdateTabUpcoming()
                val fragment = UpcomingHotelFragment()
                replaceFragment(R.id.container2, fragment, UpcomingHotelFragment::class.java.simpleName)
            }
            R.id.txtHistory -> {
                UpdateTabHistory()
                val fragment = HistoryHotelFragment()
                replaceFragment(R.id.container2, fragment, HistoryHotelFragment::class.java.simpleName)
            }
        }
    }

    fun UpdateTabUpcoming() {
        views!!.txtUpcoming.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_selected_tab_corner))
        views!!.txtHistory.setBackgroundColor(resources.getColor(R.color.colorTransparent))
    }

    fun UpdateTabHistory() {
        views!!.txtHistory.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_selected_tab_corner))
        views!!.txtUpcoming.setBackgroundColor(resources.getColor(R.color.colorTransparent))
    }
}