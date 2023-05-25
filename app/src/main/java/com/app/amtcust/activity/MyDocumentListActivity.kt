package com.app.amtcust.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.amtcust.R
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_my_document.*
import kotlinx.android.synthetic.main.activity_my_document.imgBack

class MyDocumentListActivity : BaseActivity(), View.OnClickListener {

    var sharedPreference: SharedPreference? = null

    private val repo by lazy { NetworkRepo() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_document)
        initializeView()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        initListner()
    }

    private fun initListner() {

        imgBack.setOnClickListener(this)
        fabAddDocument.setOnClickListener(this)

        IMGPANCARD.setOnClickListener(this)
        IMGAADHARCARD.setOnClickListener(this)
        IMGVISA.setOnClickListener(this)
        IMGPASSPORT.setOnClickListener(this)
        IMGOTHER.setOnClickListener(this)

        GetDocumentList1()
        GetDocumentList2()
        GetDocumentList3()
        GetDocumentList4()
        GetDocumentList5()
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.fabAddDocument -> {
                val intent = Intent(this, AddDocumentActivity::class.java)
                startActivityForResult(intent, 1001)
            }
            R.id.IMGPANCARD -> {
                val intent = Intent(this, DocumentDetailScreenActivity::class.java)
                intent.putExtra("DOCTYPE","PanCard")
                startActivity(intent)
            }
            R.id.IMGAADHARCARD -> {
                val intent = Intent(this, DocumentDetailScreenActivity::class.java)
                intent.putExtra("DOCTYPE","Aadharcard")
                startActivity(intent)
            }
            R.id.IMGVISA -> {
                val intent = Intent(this, DocumentDetailScreenActivity::class.java)
                intent.putExtra("DOCTYPE","Visa")
                startActivity(intent)
            }
            R.id.IMGPASSPORT -> {
                val intent = Intent(this, DocumentDetailScreenActivity::class.java)
                intent.putExtra("DOCTYPE","PassPort")
                startActivity(intent)
            }
            R.id.IMGOTHER -> {
                val intent = Intent(this, DocumentDetailScreenActivity::class.java)
                intent.putExtra("DOCTYPE","Other")
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
                        GetDocumentList1()
                        GetDocumentList2()
                        GetDocumentList3()
                        GetDocumentList4()
                        GetDocumentList5()
                    }
                }

            }
        }
    }

    private fun GetDocumentList1() {
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getDocumentList(userId, "PanCard",listners = ResponseListner {
            if (it.status) {
                if (it.data?.Status == 200) {

                    val arrlistPancard = it.data?.Data!!
                    txtpanCount.text = arrlistPancard!!.size.toString()

                }
            } else
                it.message.toast(this)
        })
    }

    private fun GetDocumentList2() {
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getDocumentList(userId, "Aadharcard",listners = ResponseListner {
            if (it.status) {
                if (it.data?.Status == 200) {

                    val arrlistaadhar = it.data?.Data!!
                    txtaadharCount.text = arrlistaadhar!!.size.toString()

                }
            } else
                it.message.toast(this)
        })
    }

    private fun GetDocumentList3() {
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getDocumentList(userId, "Visa",listners = ResponseListner {
            if (it.status) {
                if (it.data?.Status == 200) {

                    val arrlistVisa = it.data?.Data!!
                    txtvisaCount.text = arrlistVisa!!.size.toString()

                }
            } else
                it.message.toast(this)

        })
    }

    private fun GetDocumentList4() {

        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getDocumentList(userId, "PassPort",listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    val arrlistPassPort = it.data?.Data!!
                    txtpassppportCount.text = arrlistPassPort!!.size.toString()
                }
            } else
                it.message.toast(this)
        })
    }

    private fun GetDocumentList5() {

        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getDocumentList(userId, "Other",listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {

                    val arrlistPassPort = it.data?.Data!!
                    txtotherCount.text = arrlistPassPort!!.size.toString()

                }
            } else
                it.message.toast(this)

        })
    }
}