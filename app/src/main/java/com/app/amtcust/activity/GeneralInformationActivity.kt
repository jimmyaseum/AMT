package com.app.amtcust.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.amtcust.R
import com.app.amtcust.model.response.CustomerListModel
import com.app.amtcust.model.response.CustomerResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_general_info.*
import kotlinx.android.synthetic.main.activity_general_info.imgBack
import kotlinx.android.synthetic.main.activity_general_info.profile_image
import kotlinx.android.synthetic.main.activity_general_info.tvUserName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeneralInformationActivity : BaseActivity(),  View.OnClickListener {

    var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_info)
        initializeView()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        callCustomerDetailApi(userId)

        imgBack.setOnClickListener(this)
        imgEdit.setOnClickListener(this)
        LLEditProfile.setOnClickListener(this)
        LLcardButtonLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.imgEdit -> {
                val intent = Intent(this@GeneralInformationActivity, EditProfileActivity::class.java)
                intent.putExtra("State","Update")
                startActivityForResult(intent, 1001)
            }
            R.id.LLEditProfile -> {
                val intent = Intent(this@GeneralInformationActivity, EditProfileActivity::class.java)
                intent.putExtra("State","Update")
                startActivityForResult(intent, 1001)
            }
            R.id.LLcardButtonLogout -> {
                val sharedPreference = SharedPreference(this)
                sharedPreference.setPreference(PrefConstants.PREF_IS_LOGIN, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_ID, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_FIRST_NAME, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_LAST_NAME, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_IMAGE, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_MOBILE, "")
                sharedPreference.setPreference(PrefConstants.PREF_USER_EMAIL, "")
                sharedPreference.setPreference(PrefConstants.PREF_TOKEN, "")

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
    }

    private fun callCustomerDetailApi(userId: Int) {

        showProgress()

        val call = ApiUtils.apiInterface.getDetailByCustomers(userId)

        call.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if (response.code() == 200) {
                    var arrayList: CustomerListModel? = null
                    if (response.body()?.Status == 200) {
                        arrayList = response.body()?.Data!!
                        setAPIData(arrayList)
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: CustomerListModel) {

        tvUserName.text = arrayList.FirstName + " " + arrayList.LastName
        if(arrayList.CustomerImage != "" && arrayList.CustomerImage != null) {
            profile_image.loadUrlRoundedCorner(
                arrayList.CustomerImage,
                R.drawable.ic_profile,
                1
            )
        }

        if(arrayList.FirstName != "") {
            txtFirstName.text = arrayList.FirstName
        }
        if(arrayList.EmailID != "") {
            txtEmial.text = arrayList.EmailID
        }
        if(arrayList.MobileNo != "") {
            txtMobileNo.text = arrayList.MobileNo
        }
        var dob = ""
        if(arrayList.DOB != null) {
            dob = convertDateStringToString(
                arrayList.DOB,
                AppConstant.dd_MM_yyyy_Slash,
                AppConstant.DD_MMM_YYYY_FORMAT
            )!!
            txtDOB.text = dob
        }
        if(arrayList.Address != "") {
            txtAddress.text = arrayList.Address + " , " + arrayList.CityName + " , " + arrayList.StateName + "."
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

    override fun onResume() {
        super.onResume()
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        callCustomerDetailApi(userId)
    }
}