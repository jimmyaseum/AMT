package com.app.amtcust.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.BookingStepsAdapter
import com.app.amtcust.fragment.booking.PaxInfoFragment
import com.app.amtcust.fragment.booking.PersonalInfoFragment
import com.app.amtcust.fragment.booking.TourInfoFragment
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.BookingStepsModel
import com.app.amtcust.utils.PrefConstants
import com.app.amtcust.utils.ScreenUtils
import com.app.amtcust.utils.SharedPreference
import com.app.amtcust.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_tour_booking_form.*

class TourBookingFormActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    val arrayList = ArrayList<BookingStepsModel>()
    lateinit var adapter: BookingStepsAdapter

    var CURRENT_STEP_POSITION = 0
    private var TourBookingID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_booking_form)
        getIntentData()
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        rvOrderSteps.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        setStepsAdapter()
        selectedSteps(CURRENT_STEP_POSITION)
    }

    private fun getIntentData() {
        val sharedPreference = SharedPreference(this)

        TourBookingID = intent.getIntExtra("ID",0)

        sharedPreference.setPreference(PrefConstants.PREF_TOUR_BOOKING_ID, TourBookingID)
    }

    private fun setStepsAdapter() {

        arrayList.add(BookingStepsModel("Tour Info", true, false))
        arrayList.add(BookingStepsModel("Personal Info", false, false))
        arrayList.add(BookingStepsModel("PAX Info", false, false))

        adapter = BookingStepsAdapter( this, arrayList)
        rvOrderSteps.adapter = adapter
        adapter.setRecyclerRowClick(this)
    }

    private fun goToPreviousStep(position: Int) {
        selectedSteps(position)
    }

    //Set selection steps
    private fun selectedSteps(position: Int) {

        when (position) {
            0 -> {
                replaceFragment(TourInfoFragment(), R.id.container, TourInfoFragment::class.java.simpleName)
//                fragmentChange(TourInfoFragment.newInstance(State),0)
            }
            1 -> {
                replaceFragment(PersonalInfoFragment(), R.id.container, PersonalInfoFragment::class.java.simpleName)
//                fragmentChange(PersonalInfoFragment.newInstance(State),1)
            }
            2 -> {
                replaceFragment(PaxInfoFragment(), R.id.container, PaxInfoFragment::class.java.simpleName)
//                fragmentChange(PaxInfoFragment.newInstance(State),2)
            }

        }
    }

    private fun goToNextStep(position: Int) {
        when (position) {
            1 -> {
                replaceFragment(PersonalInfoFragment(), R.id.container, PersonalInfoFragment::class.java.simpleName)
//                fragmentChange(PersonalInfoFragment.newInstance(State),1)
            }
            2 -> {
                replaceFragment(PaxInfoFragment(), R.id.container, PaxInfoFragment::class.java.simpleName)
//                fragmentChange(PaxInfoFragment.newInstance(State),2)
            }

        }
    }

    //Update Steps color from Fragment
    fun updateStepsColor(position: Int) {
        adapter.updateStepsColor(position)
        setCenterRecyclerViewItem(position)
    }

    override fun onClick(p0: View?) {
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when (view.id) {
            R.id.llStepNumber -> {
                updateStepsColor(position)
                if (position > CURRENT_STEP_POSITION) {
                    goToNextStep(position)
                    CURRENT_STEP_POSITION = position
                } else if (position < CURRENT_STEP_POSITION) {
                    goToPreviousStep(position)
                    CURRENT_STEP_POSITION = position
                }
            }
        }
    }

    //Set RecyclerView as center item
    private fun setCenterRecyclerViewItem(position: Int) {
        (rvOrderSteps.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
            position,
            ScreenUtils.getScreenWidth(this) * 33 / 100
        )
    }

    private fun fragmentChange(fragment: Fragment, id: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, "MY_FRAGMENT").commit()
    }

}