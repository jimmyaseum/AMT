package com.app.amtcust.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.MyDocumentAdapter
import com.app.amtcust.model.response.DocumentListModel
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_document_list.*
import kotlinx.android.synthetic.main.layout_no_data.*
import java.util.ArrayList

class DocumentDetailScreenActivity : BaseActivity() {

    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: MyDocumentAdapter
    private var arrDocumentList: ArrayList<DocumentListModel> = ArrayList()

    var sharedPreference: SharedPreference? = null
    var doctype = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_list)

        doctype = intent.getStringExtra("DOCTYPE").toString()
        initializeView()

    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this)

        tbTvTitle.text = doctype + " Documents "
        imgBack.setOnClickListener {
            finish()
        }

        val layoutManager5 = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvMyDocument.layoutManager = layoutManager5

        if (isOnline(this)) {
            GetDocumentList()
        } else {
            showHideDesignView(3)
        }
    }

    private fun GetDocumentList() {
        launchProgress()
        sharedPreference = SharedPreference(this)
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getDocumentList(userId, doctype,listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrDocumentList.clear()
                    arrDocumentList = it.data?.Data!!

                    if(arrDocumentList.size > 0) {
                        rvMyDocument.adapter = MyDocumentAdapter(this, arrDocumentList)
                    }
                    disposeProgress()
                    showHideDesignView(1)
                } else if (it.data?.Status  == 1010 ||  it.data?.Status  == 201) {
                    arrDocumentList?.clear()
                    rvMyDocument.adapter?.notifyDataSetChanged()
                    showHideDesignView(2)
                    disposeProgress()
                }
            } else {
                disposeProgress()
                it.message.toast(this)
            }
        })
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
                rvMyDocument.visible()
                rlMainNoData.gone()
            }
            2 -> {
                tvNoData.text = "No Documents Found"
                imgNoData.setImageResource(R.drawable.icon_file_doc)

                rvMyDocument.gone()
                rlMainNoData.visible()
                tvRetry.gone()
            }
            3 -> {
                tvNoData.text = getString(R.string.msg_no_internet)
                imgNoData.setImageResource(R.drawable.ic_no_internet)

                rvMyDocument.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
            4 -> {
                tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                imgNoData.setImageResource(R.drawable.ic_oops)

                rvMyDocument.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
        }
    }


}