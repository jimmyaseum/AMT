package com.app.amtcust.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.amtcust.R
import com.app.amtcust.model.CustomerAppVersion
import com.app.amtcust.model.response.CommonResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import com.app.amtcust.utils.PrefConstants.PREF_IS_LOGIN
import com.app.amtcust.utils.PrefConstants.PREF_IS_WELCOME
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    var sharedPreference: SharedPreference? = null
    private var progressDialog: ProgressDialog? = null

    var id = ""
    var Type = ""

    var currentVersion = ""
    var LatestVersion = ""
    var dialog: Dialog? = null
    private var mAuth: FirebaseAuth? = null

    // commited by jimmy latest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreference = SharedPreference(applicationContext)
        mAuth = FirebaseAuth.getInstance()
        getDeviceToken()

        getCurrentVersion()

        if(isOnline(this)) {
            getLatestVersion()
        } else {
            toast(getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
        }
    }

    private fun getLatestVersion() {

        showProgress()

        val jsonObject = JSONObject()
        jsonObject.put("SettingKey", "Customer")

        val call = ApiUtils.apiInterface.getCustomerAppVersion(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CustomerAppVersion> {
            override fun onResponse(call: Call<CustomerAppVersion>, response: Response<CustomerAppVersion>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        val version = response.body()?.Data!!
                        LatestVersion = version.Version

                        if(currentVersion == LatestVersion) {
                            changeActivitys()
                        } else {
                            if (sharedPreference?.getPreferenceString(PREF_IS_LOGIN).equals("1")) {
                                callLogoutAPI()
                            } else {
                                showUpdateDialog()
                            }
                        }

                    } else {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }
            override fun onFailure(call: Call<CustomerAppVersion>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun changeActivitys() {
        Handler().postDelayed(Runnable {

            if (sharedPreference?.getPreferenceString(PREF_IS_WELCOME).equals("1")) {

                if (sharedPreference?.getPreferenceString(PREF_IS_LOGIN).equals("1")) {
                    if (intent.extras != null && intent.hasExtra("ID")) {

                        id = intent.extras!!.getString("ID")!!
                        Type = intent.extras!!.getString("NotificationType")!!

                        if(Type == "HOTELVOUCHER") {
                            val intent = Intent(this, MyVoucherListActivity::class.java)
                            intent.putExtra("NotificationType", Type)
                            startActivity(intent)
                            finish()
                        }
                        else if(Type == "AIRLINEVOUCHER") {
                            val intent = Intent(this, FlightVoucherDetailActivity::class.java)
                            intent.putExtra("ID",id.toInt())
                            startActivity(intent)
                            finish()
                        }
                        else if (Type == "ROUTEVOUCHER") {
                            val intent = Intent(this, RouteVoucherDetailsActivity::class.java)
                            intent.putExtra("ID",id.toInt())
                            startActivity(intent)
                            finish()
                        }
                        else if(Type == "PAYMENTRECEIPT") {
                            val intent = Intent(this, PaymentReceiptDetailsActivity::class.java)
                            intent.putExtra("ID",id.toInt())
                            startActivity(intent)
                        }
                        else if(Type == "CANCELLATIONBOOKING") {
                            val intent = Intent(this, TourBookingFormActivity::class.java)
                            intent.putExtra("ID",id.toInt())
                            startActivity(intent)
                            finish()
                        }
                        else {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    val intent = Intent(applicationContext, StartActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            } else {
                val intent = Intent(applicationContext, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }

    private fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                LoginActivity.fcmDeviceToken = task.result!!.token

                Log.e("FCM TOKEN","===>"+task.result!!.token)
            })
    }

    private fun getCurrentVersion() {
        val pm = this.packageManager
        var pInfo: PackageInfo? = null
        try {
            pInfo = pm.getPackageInfo(this.packageName, 0)
        } catch (e1: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        currentVersion = pInfo!!.versionName

    }

    private fun showUpdateDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Please Update")
        builder.setMessage("We have launched a new and improved version. Please update the app to continue using the app.")
        builder.setPositiveButton("Update Now",
            DialogInterface.OnClickListener { dialog, which ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.app.amtcust")
                    )
                )
                dialog.dismiss()
            })
        builder.setCancelable(false)
        dialog = builder.show()
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
                        val sharedPreference = SharedPreference(this@SplashActivity)
                        sharedPreference.setPreference(PrefConstants.PREF_IS_LOGIN, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_ID, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_FIRST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_LAST_NAME, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_IMAGE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_MOBILE, "")
                        sharedPreference.setPreference(PrefConstants.PREF_USER_EMAIL, "")
                        sharedPreference.setPreference(PrefConstants.PREF_TOKEN, "")

                        mAuth!!.signOut()
                        showUpdateDialog()
                    } else {
                        hideProgress()
                        toast(response.body()?.Details.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })

    }

    fun hideProgress() {
        if (progressDialog != null)
            progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    fun showProgress() {
        hideProgress()
        progressDialog = CommonUtil.showLoadingDialog(this)
    }

}
