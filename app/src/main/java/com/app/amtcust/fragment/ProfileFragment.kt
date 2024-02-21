package com.app.amtcust.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtcust.Chat.ChatBoatActivity
import com.app.amtcust.R
import com.app.amtcust.activity.*
import com.app.amtcust.model.response.CommonResponse
import com.app.amtcust.model.response.CustomerListModel
import com.app.amtcust.model.response.CustomerResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.retrofit.ApiUtils.apiInterface
import com.app.amtcust.utils.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.profile_image
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : BaseFragment(), View.OnClickListener {
    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_profile, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        sharedPreference = SharedPreference(activity!!)
        mAuth = FirebaseAuth.getInstance()
        val OnGoingTour = sharedPreference?.getPreferenceInt("OnGoingTour")
        if(OnGoingTour != 0) {
            views!!.LL_Chat.visible()
        } else {
            views!!.LL_Chat.gone()
        }

        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        
        callCustomerDetailApi(userId)

        views!!.LLEditProfile.setOnClickListener(this)
        views!!.LL_Family_Member.setOnClickListener(this)
        views!!.LL_Company.setOnClickListener(this)
        views!!.LL_My_Document.setOnClickListener(this)
        views!!.LL_Logout.setOnClickListener(this)
        views!!.LL_Chat.setOnClickListener(this)
        views!!.LL_General_Info.setOnClickListener(this)
        views!!.LL_My_Booking.setOnClickListener(this)
        views!!.LL_My_Voucher.setOnClickListener(this)
        views!!.LL_Payments_Receipt.setOnClickListener(this)
        views!!.LL_Review.setOnClickListener(this)
        views!!.LL_Notification.setOnClickListener(this)

    }

    private fun callCustomerDetailApi(userId: Int) {

        showProgress()

        val call = apiInterface.getDetailByCustomers(userId)

        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        if(arrayList.FirstName != null) {
                            setAPIData(arrayList)
                        }
                    }
                    hideProgress()
                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

    private fun setAPIData(arrayList: CustomerListModel) {

        if(arrayList.FirstName != null  && arrayList.FirstName != "") {
            if(arrayList.LastName != null && arrayList.LastName != "") {
                tvUserName.text = arrayList.FirstName + " " + arrayList.LastName
            } else {
                tvUserName.text = arrayList.FirstName
            }
        }

        if(arrayList.CustomerImage != "" && arrayList.CustomerImage != null) {
            profile_image.loadUrlRoundedCorner(
                arrayList.CustomerImage,
                R.drawable.ic_profile,
                1
            )
        }
    }

    override fun onClick(v: View?) {
        hideKeyboard(activity!!, v)
        when (v?.id) {
            R.id.LLEditProfile -> {
                val intent = Intent(activity, EditProfileActivity::class.java)
                intent.putExtra("State","Update")
                startActivityForResult(intent, 1001)
            }
            R.id.LL_Company -> {
                val intent = Intent(activity!!, AddCompanyDetailsActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_Family_Member -> {
                val intent = Intent(activity!!, FamilyMemberListActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_My_Booking -> {
                val intent = Intent(activity!!, MyTripListActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_My_Voucher -> {
                val intent = Intent(activity!!, MyVoucherListActivity::class.java)
                intent.putExtra("NotificationType","")
                startActivity(intent)
            }
            R.id.LL_Payments_Receipt -> {
                val intent = Intent(activity!!, PaymentReceiptListActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_My_Document -> {
                val intent = Intent(activity!!, MyDocumentListActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_Logout -> {
                callLogoutAPI()
            }
            R.id.LL_Chat -> {
                val intent = Intent(activity!!, ChatBoatActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_General_Info -> {
                val intent = Intent(activity!!, GeneralInformationActivity::class.java)
                startActivity(intent)
            }
            R.id.LL_Review -> {
                var fullpath = "https://g.page/r/CWuTPp1IRKA6EB0/review"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullpath))
                startActivity(browserIntent)
            }
            R.id.LL_Notification -> {
                val intent = Intent(activity!!, NotificationListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val isCallAPI = data!!.getBooleanExtra(AppConstant.IS_API_CALL, false)
                    if (isCallAPI) {
                        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
                        callCustomerDetailApi(userId)
                    }
                }

            }
        }
    }
    private fun callLogoutAPI() {

        showProgress()

        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()

        val jsonObject = JSONObject()
        jsonObject.put("ID", userId)

        val call = ApiUtils.apiInterface.logout(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.code == 200) {
                        hideProgress()
                        val sharedPreference = SharedPreference(activity!!)
                        sharedPreference.setPreference(PrefConstants.PREF_IS_LOGIN, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_ID, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_FIRST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_LAST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_IMAGE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_MOBILE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_EMAIL, "")
                        sharedPreference.setPreference(PrefConstants.PREF_TOKEN, "")

                        mAuth!!.signOut()

                        val intent = Intent(activity!!, LoginActivity::class.java)
                        activity!!.startActivity(intent)
                        activity!!.finishAffinity()
                    } else {
                        hideProgress()
                        activity!!.toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                LogUtil.e(TAG, "=====onFailure====" + t)
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }
}