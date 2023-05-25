package com.app.amtcust.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.FamilyMemberListAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.CommonResponse
import com.app.amtcust.model.response.FamilyMemberListModel
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_family_member_list.*
import kotlinx.android.synthetic.main.activity_family_member_list.imgBack
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FamilyMemberListActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    var sharedPreference: SharedPreference? = null

    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: FamilyMemberListAdapter
    private var arrFamilyMemberList: ArrayList<FamilyMemberListModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_member_list)
        initializeView()
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(this)
        setToolBar()
        initListner()
    }

    private fun initListner() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvFamilyMemberList.layoutManager = layoutManager

        if (isOnline(this)) {
            GetFamilyMemberList()
        } else {
            showHideDesignView(3)
        }

        swipeRefreshFamilyMember.setOnRefreshListener {
            if (isOnline(this)) {
                GetFamilyMemberList()
            } else {
                showHideDesignView(3)
            }
        }
    }

    private fun setToolBar() {

        imgBack.setOnClickListener(this)
        fabAddContact.setOnClickListener(this)
        tvRetry.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.fabAddContact -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                intent.putExtra("State","Add")
                startActivityForResult(intent, 1001)

            }
            R.id.tvRetry -> {
                if (isOnline(this)) {
                    GetFamilyMemberList()
                } else {
                    showHideDesignView(3)
                }
            }
        }
    }

    private fun GetFamilyMemberList() {
        launchProgress()
        val userId = sharedPreference?.getPreferenceString(PrefConstants.PREF_USER_ID)!!.toInt()
        repo.getFamilyMemberList(userId, listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrFamilyMemberList.clear()
                    arrFamilyMemberList = it.data?.Data!!

                    if(arrFamilyMemberList.size > 0) {
                        rvFamilyMemberList.adapter = FamilyMemberListAdapter(this,arrFamilyMemberList,this)
                        showHideDesignView(1)
                        swipeRefreshFamilyMember.isRefreshing = false
                    }

                    disposeProgress()
                } else if (it.data?.Status  == 1010 ||  it.data?.Status  == 201) {
                    showHideDesignView(2)
                    arrFamilyMemberList?.clear()
                    rvFamilyMemberList.adapter?.notifyDataSetChanged()
                    swipeRefreshFamilyMember.isRefreshing = false
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
            110 -> {
                val popupMenu: PopupMenu = PopupMenu(this,view)
                popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.menuEdit -> {
                            popupMenu.dismiss()
                            val intent = Intent(this, EditProfileActivity::class.java)
                            intent.putExtra("State", "FamilyUpdate")
                            intent.putExtra("CustometID",arrFamilyMemberList[position].ID)
                            startActivityForResult(intent, 1001)
                        }
                        R.id.menuDelete -> {
                            popupMenu.dismiss()
                            val builder = AlertDialog.Builder(this@FamilyMemberListActivity)
                            builder.setMessage("Are you sure you want to Delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes") { dialog, id ->
                                    // Delete selected note from database
                                    CallDeleteCustomerAPI(arrFamilyMemberList[position].ID)
                                }
                                .setNegativeButton("No") { dialog, id ->
                                    // Dismiss the dialog
                                    dialog.dismiss()
                                }
                            val alert = builder.create()
                            alert.show()
                    }
                    }
                    true
                })
                popupMenu.show()
            }
        }
    }

    private fun CallDeleteCustomerAPI(userId: Int) {

        showProgress()

        var jsonObject = JSONObject()
        jsonObject.put("ID", userId)

        val call = ApiUtils.apiInterface.getCustomersDelete(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.code == 200) {
                        GetFamilyMemberList()
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val isCallAPI = data!!.getBooleanExtra(AppConstant.IS_API_CALL, false)
                    if (isCallAPI) {
                        if (isOnline(this)) {
                            GetFamilyMemberList()
                        } else {
                            showHideDesignView(3)
                        }
                    }
                }
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
                rvFamilyMemberList.visible()
                rlMainNoData.gone()
            }
            2 -> {

                tvNoData.text = "No Family Member Found"
                imgNoData.setImageResource(R.drawable.ic_couple_tour)

                rvFamilyMemberList.gone()
                rlMainNoData.visible()
                tvRetry.gone()
            }
            3 -> {
                tvNoData.text = getString(R.string.msg_no_internet)
                imgNoData.setImageResource(R.drawable.ic_no_internet)

                rvFamilyMemberList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
            4 -> {
                tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                imgNoData.setImageResource(R.drawable.ic_oops)

                rvFamilyMemberList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
        }
    }

}