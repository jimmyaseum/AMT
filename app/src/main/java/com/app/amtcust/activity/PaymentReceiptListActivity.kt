package com.app.amtcust.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.PaymentReceiptListAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.PaymentReceiptListModel
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_payment_receipt_list.*
import kotlinx.android.synthetic.main.layout_no_data.*

class PaymentReceiptListActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {
    var sharedPreference: SharedPreference? = null

    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: PaymentReceiptListAdapter
    private var arrPaymentReceiptList: ArrayList<PaymentReceiptListModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_receipt_list)
        initializeView()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        setToolBar()
        initListner()
    }

    private fun initListner() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvPaymentReceiptList.layoutManager = layoutManager

        if (isOnline(this)) {
            GetPaymentReceiptList()
        } else {
            showHideDesignView(3)
        }

        swipeRefreshPaymentReceipt.setOnRefreshListener {
            if (isOnline(this)) {
                GetPaymentReceiptList()
            } else {
                showHideDesignView(3)
            }
        }
    }

    private fun setToolBar() {

        imgBack.setOnClickListener(this)
        tvRetry.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.tvRetry -> {
                if (isOnline(this)) {
                    GetPaymentReceiptList()
                } else {
                    showHideDesignView(3)
                }
            }
        }
    }

    private fun GetPaymentReceiptList() {
        launchProgress()
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getPaymentReceiptList(userId, listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrPaymentReceiptList.clear()
                    arrPaymentReceiptList = it.data?.Data!!

                    if(arrPaymentReceiptList.size > 0) {
                        rvPaymentReceiptList.adapter = PaymentReceiptListAdapter(this,arrPaymentReceiptList,this)
                        showHideDesignView(1)
                        swipeRefreshPaymentReceipt.isRefreshing = false
                    }

                    disposeProgress()
                } else if (it.data?.Status  == 1010 ||  it.data?.Status  == 201) {
                    showHideDesignView(2)
                    arrPaymentReceiptList?.clear()
                    rvPaymentReceiptList.adapter?.notifyDataSetChanged()
                    swipeRefreshPaymentReceipt.isRefreshing = false
                    disposeProgress()
                }
            } else {
                it.message.toast(this)
                disposeProgress()
            }
        })
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when (type) {
            112 -> {
                val intent = Intent(this, PaymentReceiptDetailsActivity::class.java)
                intent.putExtra("ID",arrPaymentReceiptList[position].ID)
//                intent.putExtra("TourBookingNo",arrPaymentReceiptList[position].BookingNo)
                startActivity(intent)
            }
        }
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
                rvPaymentReceiptList.visible()
                rlMainNoData.gone()
            }
            2 -> {

                tvNoData.text = "No Payment Receipt Found"
                imgNoData.setImageResource(R.drawable.ic_couple_tour)

                rvPaymentReceiptList.gone()
                rlMainNoData.visible()
                tvRetry.gone()
            }
            3 -> {
                tvNoData.text = getString(R.string.msg_no_internet)
                imgNoData.setImageResource(R.drawable.ic_no_internet)

                rvPaymentReceiptList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
            4 -> {
                tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                imgNoData.setImageResource(R.drawable.ic_oops)

                rvPaymentReceiptList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
        }
    }

}