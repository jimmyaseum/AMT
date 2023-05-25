package com.app.amtcust.activity

import android.os.Bundle
import com.app.amtcust.R
import com.app.amtcust.adapter.MyVoucherAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_my_voucher_list.*

class MyVoucherListActivity : BaseActivity() {
    var Type : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_voucher_list)
        Type = intent.getStringExtra("NotificationType").toString()
        initializeView()
    }

    override fun initializeView() {

        imgBack.setOnClickListener {
            finish()
        }

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Hotel"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Flight"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Route"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = MyVoucherAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        if(Type.equals("HOTELVOUCHER")) {
            viewPager!!.currentItem = 0
        }
        else if (Type.equals("AIRLINEVOUCHER")) {
            viewPager!!.currentItem = 1
        }
        else if (Type.equals("ROUTEVOUCHER")) {
            viewPager!!.currentItem = 2
        }
        else {
            viewPager!!.currentItem = 0
        }
    }
}