package com.app.amtcust.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.amtcust.R
import com.app.amtcust.adapter.NotificationListAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.NotificationListModel
import com.app.amtcust.model.response.NotificationListResponse
import com.app.amtcust.model.response.PaymentReceiptDetailsModel
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class NotificationListActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {
    var sharedPreference: SharedPreference? = null

    private var arrList: ArrayList<NotificationListModel>? = null
    lateinit var adapter: NotificationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        initializeView()
    }
    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        initListner()
    }

    private fun initListner() {

        imgBack.setOnClickListener(this)
        tvRetry.setOnClickListener(this)

        arrList = ArrayList()
        rvNotificationList.layoutManager = LinearLayoutManager(this)

        if (isOnline(this@NotificationListActivity)) {
            CallNotificationAPI()
        } else {
            showHideDesignView(3)
        }

    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.tvRetry -> {
                if (isOnline(this@NotificationListActivity)) {
                    CallNotificationAPI()
                } else {
                    showHideDesignView(3)
                }
            }
        }
    }

    private fun CallNotificationAPI() {

        showProgress()

        if (sharedPreference == null) {
            sharedPreference = SharedPreference(this)
        }

        val CreatedBy = sharedPreference!!.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        var jsonObject = JSONObject()
        jsonObject.put("UserID",CreatedBy)
        jsonObject.put("UserType",1)

        val call = ApiUtils.apiInterface.getNotificationList(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<NotificationListResponse> {
            override fun onFailure(call: Call<NotificationListResponse>, t: Throwable) {
                hideProgress()
                showHideDesignView(4)
            }
            override fun onResponse(call: Call<NotificationListResponse>, response: Response<NotificationListResponse>) {
                if (response.code() == 200) {
                    if (response.body()!!.Status == 200) {
                        val arrayListdashboard = response.body()?.Data

                        arrList!!.clear()
                        arrList = arrayListdashboard!!

                        adapter = NotificationListAdapter(this@NotificationListActivity,
                            arrList!!,this@NotificationListActivity)
                        rvNotificationList.adapter = adapter

                        hideProgress()
                        showHideDesignView(1)
                    } else if (response.body()!!.Status == 1010 ||  response.body()?.Status == 201) {
                        hideProgress()
                        showHideDesignView(2)
                    }
                } else {
                    hideProgress()
                    showHideDesignView(2)
                }
            }
        })
    }

    private fun showHideDesignView(mode: Int) {

        when (mode) {
            1 -> {
                rvNotificationList.visible()
                rlMainNoData.gone()
            }
            2 -> {

                tvNoData.text = "No Data Found"
                imgNoData.setImageResource(R.drawable.ic_notifications)

                rvNotificationList.gone()
                rlMainNoData.visible()
                tvRetry.gone()
            }
            3 -> {
                tvNoData.text = getString(R.string.msg_no_internet)
                imgNoData.setImageResource(R.drawable.ic_no_internet)

                rvNotificationList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
            4 -> {
                tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                imgNoData.setImageResource(R.drawable.ic_oops)

                rvNotificationList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
        }
    }

    override fun onItemClickEvent(view: View, position: Int, typ: Int) {

        when(typ) {
            108 -> {
                val type = arrList!![position].MessageType

                if(type.equals("HOTELVOUCHER")) {
                    val intent = Intent(this, MyVoucherListActivity::class.java)
                    intent.putExtra("ID",arrList!![position].ReferenceID)
                    intent.putExtra("NotificationType",type)
                    startActivity(intent)
                }
                else if(type.equals("AIRLINEVOUCHER")) {
                    val intent = Intent(this, FlightVoucherDetailActivity::class.java)
                    intent.putExtra("ID",arrList!![position].ReferenceID)
                    intent.putExtra("NotificationType",type)
                    startActivity(intent)
                }
                else if(type.equals("ROUTEVOUCHER")) {
                    val intent = Intent(this, RouteVoucherDetailsActivity::class.java)
                    intent.putExtra("ID",arrList!![position].ReferenceID)
                    intent.putExtra("NotificationType",type)
                    startActivity(intent)
                }
                else if(type.equals("PAYMENTRECEIPT")) {
                    val intent = Intent(this, PaymentReceiptDetailsActivity::class.java)
                    intent.putExtra("ID",arrList!![position].ReferenceID)
                    intent.putExtra("NotificationType",type)
                    startActivity(intent)
                }
                else if(type.equals("CANCELLATIONBOOKING")) {
                    val intent = Intent(this, TourBookingFormActivity::class.java)
                    intent.putExtra("ID",arrList!![position].ReferenceID)
                    intent.putExtra("NotificationType",type)
                    startActivity(intent)
                }
            }
        }


    }


}