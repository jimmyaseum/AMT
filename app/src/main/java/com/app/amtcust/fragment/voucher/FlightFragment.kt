package com.app.amtcust.fragment.voucher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtcust.R
import com.app.amtcust.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_flight.view.*

class FlightFragment : BaseFragment(), View.OnClickListener {

    private var views: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_flight, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        initListeners()
    }

    private fun initListeners() {
        views!!.txtPrevious.setOnClickListener(this)
        views!!.txtFuture.setOnClickListener(this)

        val fragment = FutureFlightFragment()
        replaceFragment(R.id.container3, fragment, FutureFlightFragment::class.java.simpleName)
        UpdateTabFuture()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txtPrevious -> {
                UpdateTabPrevious()
                val fragment = PreviousFlightFragment()
                replaceFragment(R.id.container3, fragment, PreviousFlightFragment::class.java.simpleName)
            }
            R.id.txtFuture -> {
                UpdateTabFuture()
                val fragment = FutureFlightFragment()
                replaceFragment(R.id.container3, fragment, FutureFlightFragment::class.java.simpleName)
            }
        }
    }

    fun UpdateTabPrevious() {
        views!!.txtPrevious.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_selected_tab_corner))
        views!!.txtFuture.setBackgroundColor(resources.getColor(R.color.colorTransparent))
    }

    fun UpdateTabFuture() {
        views!!.txtFuture.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_selected_tab_corner))
        views!!.txtPrevious.setBackgroundColor(resources.getColor(R.color.colorTransparent))
    }
}