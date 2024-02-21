package com.app.amtcust.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.DestinationListAdapter
import com.app.amtcust.adapter.dialog.DialogInitialAdapter
import com.app.amtcust.adapter.dialog.DialogSectorAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.SectorListModel
import com.app.amtcust.model.response.SectorListResponse
import com.app.amtcust.model.response.TourDestinationListModel
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.activity_destination_list.*
import kotlinx.android.synthetic.main.activity_destination_list.imgBack
import kotlinx.android.synthetic.main.layout_no_data.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class DestinationListActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    var SpecialityFilters = ""
    var RoomTypeFilters = ""
    var DurationFilters = ""
    var MinFilters = "0"
    var MaxFilters = "200000"
//    var BudgetFilters = "Rate BETWEEN 0 AND 200000"
    var OrderBy = "ORDER BY Rate ASC"
    var Count = 0
    var Sortby_Name = "Price: Low To High"

    var redionId = ""
    var sectorURL = ""
    var PageIndex = 1
    var PageSize = 30
    var tourType = ""
    var Himalayatrek = ""
    var IsSearch = ""

    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: DestinationListAdapter
    private var arrTourDestinationList: ArrayList<TourDestinationListModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_list)
        getIntentData()
        initializeView()
    }

    private fun getIntentData() {
        redionId = intent.getStringExtra("REGIONID").toString()
        tourType = intent.getStringExtra("TRAVELTYPE").toString()
        sectorURL = intent.getStringExtra("SECTORURL").toString()
        Himalayatrek = intent.getStringExtra("HIMALAYATREK").toString()
        IsSearch = intent.getStringExtra("ISSEARCH").toString()
    }

    override fun initializeView() {
        setToolBar()
        initListner()
    }

    private fun initListner() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvDestinationList.layoutManager = layoutManager

        if (isOnline(this)) {
            CallGetTourDestinationListAPI()
        } else {
            showHideDesignView(3)
        }

        swipeRefreshDestination.setOnRefreshListener {
            if (isOnline(this)) {
                CallGetTourDestinationListAPI()
            } else {
                showHideDesignView(3)
            }
        }
    }

    private fun setToolBar() {
        imgBack.setOnClickListener(this)
        LL_Sort_By.setOnClickListener(this)
        LL_Filter.setOnClickListener(this)
        tvRetry.setOnClickListener(this)

        tvFilterCount.text = "" + Count +" Selected"
        tvSortByName.text = Sortby_Name
    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.LL_Filter -> {
                // Pass Intent filter activity
                val intent = Intent(applicationContext, TourListFilterActivity::class.java)

                Log.e("MinFilter put","===>"+MinFilters)
                Log.e("MaxFilters put","===>"+MaxFilters)

                intent.putExtra(AppConstant.SpecialityFilters, SpecialityFilters)
                intent.putExtra(AppConstant.RoomTypeFilters, RoomTypeFilters)
                intent.putExtra(AppConstant.DurationFilters, DurationFilters)
                intent.putExtra(AppConstant.MinFilters,MinFilters)
                intent.putExtra(AppConstant.MaxFilters,MaxFilters)
//                intent.putExtra(AppConstant.BudgetFilters, BudgetFilters)
                startActivityForResult(intent, 1)
            }
            R.id.LL_Sort_By -> {
                selectSortDialog()
            }

            R.id.tvRetry -> {
                if (isOnline(this)) {
                    CallGetTourDestinationListAPI()
                } else {
                    showHideDesignView(3)
                }
            }
        }
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when (type) {
            108 -> {
                val intent = Intent(this, DestinationDetailsActivity::class.java)
                intent.putExtra("TourID",arrTourDestinationList[position].ID)
                intent.putExtra("TourURL",arrTourDestinationList[position].TourURL)
                intent.putExtra("RateType",arrTourDestinationList[position].Ratetype)
                intent.putExtra("NoOfNights",arrTourDestinationList[position].NoOfNights)
                intent.putExtra("RoomTypeID",arrTourDestinationList[position].RoomTypeID)
                startActivity(intent)

                //{
                //  "TourID": 10,
                //  "RateType": "Per Person",
                //  "NoOfNights":"5"
                //}
            }
            109 -> {
                val intent = Intent(this, DestinationDetailsActivity::class.java)
                intent.putExtra("TourID",arrTourDestinationList[position].ID)
                intent.putExtra("TourURL",arrTourDestinationList[position].TourURL)
                intent.putExtra("RateType",arrTourDestinationList[position].Ratetype)
                intent.putExtra("NoOfNights",arrTourDestinationList[position].NoOfNights)
                intent.putExtra("RoomTypeID",arrTourDestinationList[position].RoomTypeID)
                startActivity(intent)
            }
            110 -> {
                selectDialog()
            }

        }
    }

    private fun selectDialog() {

        val arrTypeList: ArrayList<String> = ArrayList()
        var arrSectorList: ArrayList<SectorListModel> = ArrayList()

        val repo by lazy { NetworkRepo() }

        val dialogMember = Dialog(this)
        dialogMember.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_inquiry_now, null)
        dialogMember.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogMember.window!!.attributes)

        dialogMember.window!!.attributes = lp
        dialogMember.setCancelable(true)
        dialogMember.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogMember.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogMember.window!!.setGravity(Gravity.CENTER)

        val etName = dialogMember.findViewById(R.id.etName) as EditText
        val etEmail = dialogMember.findViewById(R.id.etEmail) as EditText
        val etMobile = dialogMember.findViewById(R.id.etMobile) as EditText
        val etType = dialogMember.findViewById(R.id.etType) as EditText
        val etDestination = dialogMember.findViewById(R.id.etDestination) as EditText

        val ImgClose = dialogMember.findViewById(R.id.ImgClose) as ImageView
        val CardCancel = dialogMember.findViewById(R.id.CardCancel) as CardView
        val CardInquiryNow = dialogMember.findViewById(R.id.CardInquiryNow) as CardView

        // AI005 Mr, Mrs, Ms, Dr
        arrTypeList.add("Domestic")
        arrTypeList.add("International")

        ImgClose.setOnClickListener {

            dialogMember.dismiss()
        }
        CardCancel.setOnClickListener {

            dialogMember.dismiss()
        }

        etDestination.setOnClickListener {
            if(arrSectorList.size > 0) {
                var dialogSelectCity = Dialog(this)
                dialogSelectCity.requestWindowFeature(Window.FEATURE_NO_TITLE)

                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select, null)
                dialogSelectCity.setContentView(dialogView)

                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialogSelectCity.window!!.attributes)

                dialogSelectCity.window!!.attributes = lp
                dialogSelectCity.setCancelable(true)
                dialogSelectCity.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                dialogSelectCity.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                dialogSelectCity.window!!.setGravity(Gravity.CENTER)

                val rvDialogCustomer = dialogSelectCity.findViewById(R.id.rvDialogCustomer) as RecyclerView
                val edtSearchCustomer = dialogSelectCity.findViewById(R.id.edtSearchCustomer) as EditText

                val itemAdapter = DialogSectorAdapter(this, arrSectorList!!)
                itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                    override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                        etDestination.setText(arrSectorList[pos].SectorName)
                        dialogSelectCity!!.dismiss()
                    }
                })

                rvDialogCustomer.adapter = itemAdapter

                edtSearchCustomer.gone()
                dialogSelectCity!!.show()
            } else {
                var jsonObject = JSONObject()
                jsonObject.put("SectorType", etType.text.toString())

                val call = ApiUtils.apiInterface.getAllSector(getRequestJSONBody(jsonObject.toString()))

                call.enqueue(object : Callback<SectorListResponse> {
                    override fun onResponse(call: Call<SectorListResponse>, response: Response<SectorListResponse>) {

                        if (response.code() == 200) {
                            LogUtil.e(TAG, "=====onResponse====")

                            if (response.body()?.Status == 200) {
                                hideProgress()
                                arrSectorList = response.body()?.Data!!
                                if(arrSectorList!!.size > 0) {
                                    etDestination.performClick()
                                } else {
                                    toast("No Value Available.", AppConstant.TOAST_SHORT)
                                }

                            } else {
                                toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                                hideProgress()
                            }
                        }
                    }

                    override fun onFailure(call: Call<SectorListResponse>, t: Throwable) {
                        hideProgress()
                        toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
                    }
                })
            }
        }

        etType.setText(arrTypeList[0])
        etType.setOnClickListener {
            var dialogSelectInitial = Dialog(this)
            dialogSelectInitial.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select, null)
            dialogSelectInitial.setContentView(dialogView)

            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialogSelectInitial.window!!.attributes)

            dialogSelectInitial.window!!.attributes = lp
            dialogSelectInitial.setCancelable(true)
            dialogSelectInitial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogSelectInitial.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            dialogSelectInitial.window!!.setGravity(Gravity.CENTER)

            val rvDialogCustomer = dialogSelectInitial.findViewById(R.id.rvDialogCustomer) as RecyclerView
            val edtSearchCustomer = dialogSelectInitial.findViewById(R.id.edtSearchCustomer) as EditText

            val itemAdapter = DialogInitialAdapter(this, arrTypeList!!)
            itemAdapter.setRecyclerRowClick(object : RecyclerClickListener {
                override fun onItemClickEvent(v:View, pos: Int, flag: Int) {
                    etType.setText(arrTypeList!![pos])
                    dialogSelectInitial!!.dismiss()
                }

            })

            rvDialogCustomer.adapter = itemAdapter

            edtSearchCustomer.gone()
            dialogSelectInitial!!.show()
        }
        dialogMember!!.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                //TourListFilter Activity
                1 -> {

                    SpecialityFilters = data!!.getStringExtra(AppConstant.SpecialityFilters).toString()
                    RoomTypeFilters = data!!.getStringExtra(AppConstant.RoomTypeFilters).toString()
                    DurationFilters = data.getStringExtra(AppConstant.DurationFilters).toString()
                    MinFilters = data.getStringExtra(AppConstant.MinFilters).toString()
                    MaxFilters = data.getStringExtra(AppConstant.MaxFilters).toString()
//                    BudgetFilters = data.getStringExtra(AppConstant.BudgetFilters).toString()
                    Count = data.getIntExtra("Count",0)

                    Log.e("MinFilter get","===>"+MinFilters)
                    Log.e("MaxFilters get","===>"+MaxFilters)

                    tvFilterCount.text = "" + Count +" Selected"

                    if (isOnline(this)) {
                        CallGetTourDestinationListAPI()
                    } else {
                        showHideDesignView(3)
                    }
                }

            }
        }
    }

    /** AI005
     * This method is to open Initial dialog
     */
    private fun selectSortDialog() {
        var dialogSelectInitial = Dialog(this)
        dialogSelectInitial.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogView = layoutInflater.inflate(R.layout.dialog_sort_by, null)
        dialogSelectInitial.setContentView(dialogView)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSelectInitial.window!!.attributes)

        dialogSelectInitial.window!!.attributes = lp
        dialogSelectInitial.setCancelable(true)
        dialogSelectInitial.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogSelectInitial.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialogSelectInitial.window!!.setGravity(Gravity.CENTER)

        val radioPriceASC = dialogSelectInitial.findViewById(R.id.radioPriceASC) as RadioButton
        val radioPriceDESC = dialogSelectInitial.findViewById(R.id.radioPriceDESC) as RadioButton
        val radioDurationASC = dialogSelectInitial.findViewById(R.id.radioDurationASC) as RadioButton
        val radioDurationDESC = dialogSelectInitial.findViewById(R.id.radioDurationDESC) as RadioButton

        if(tvSortByName.text.equals("Price: Low To High")) {
            radioPriceASC.isChecked = true
            radioPriceDESC.isChecked = false
            radioDurationASC.isChecked = false
            radioDurationDESC.isChecked = false
        }
        else if(tvSortByName.text.equals("Price: High To Low")) {
            radioPriceASC.isChecked = false
            radioPriceDESC.isChecked = true
            radioDurationASC.isChecked = false
            radioDurationDESC.isChecked = false
        }
        else if(tvSortByName.text.equals("Duration: Low To High")) {
            radioPriceASC.isChecked = false
            radioPriceDESC.isChecked = false
            radioDurationASC.isChecked = true
            radioDurationDESC.isChecked = false
        }
        else if(tvSortByName.text.equals("Duration: High To Low")) {
            radioPriceASC.isChecked = false
            radioPriceDESC.isChecked = false
            radioDurationASC.isChecked = false
            radioDurationDESC.isChecked = true
        }

        radioPriceASC.setOnClickListener {
            radioPriceASC.isChecked = true
            radioPriceDESC.isChecked = false
            radioDurationASC.isChecked = false
            radioDurationDESC.isChecked = false

            tvSortByName.text = radioPriceASC.text
            OrderBy = "ORDER BY Rate ASC"
            dialogSelectInitial.dismiss()

            if (isOnline(this)) {
                CallGetTourDestinationListAPI()
            } else {
                showHideDesignView(3)
            }
        }
        radioPriceDESC.setOnClickListener {
            radioPriceASC.isChecked = false
            radioPriceDESC.isChecked = true
            radioDurationASC.isChecked = false
            radioDurationDESC.isChecked = false

            tvSortByName.text = radioPriceDESC.text
            OrderBy = "ORDER BY Rate DESC"
            dialogSelectInitial.dismiss()

            if (isOnline(this)) {
                CallGetTourDestinationListAPI()
            } else {
                showHideDesignView(3)
            }
        }
        radioDurationASC.setOnClickListener {
            radioPriceASC.isChecked = false
            radioPriceDESC.isChecked = false
            radioDurationASC.isChecked = true
            radioDurationDESC.isChecked = false

            tvSortByName.text = radioDurationASC.text
            OrderBy = "ORDER BY NoOfDays ASC"
            dialogSelectInitial.dismiss()

            if (isOnline(this)) {
                CallGetTourDestinationListAPI()
            } else {
                showHideDesignView(3)
            }
        }

        radioDurationDESC.setOnClickListener {
            radioPriceASC.isChecked = false
            radioPriceDESC.isChecked = false
            radioDurationASC.isChecked = false
            radioDurationDESC.isChecked = true

            tvSortByName.text = radioDurationDESC.text
            OrderBy = "ORDER BY NoOfDays DESC"
            dialogSelectInitial.dismiss()

            if (isOnline(this)) {
                CallGetTourDestinationListAPI()
            } else {
                showHideDesignView(3)
            }
        }

        dialogSelectInitial!!.show()
    }

    private fun CallGetTourDestinationListAPI() {
        launchProgress()

        val budgetfilter = "Rate BETWEEN "+ MinFilters +" AND " + MaxFilters

        if (intent.getBooleanExtra("ISCOUPLETOUR", false) == true) {
            repo.getCoupleTourDestinationList(
                redionId,
                RoomTypeFilters,
                Himalayatrek,
                budgetfilter,
                DurationFilters,
                SpecialityFilters,
                OrderBy,
                IsSearch,
                sectorURL,
                PageIndex,
                PageSize,
                listners = ResponseListner {

                    if (it.status) {
                        if (it.data?.Status == 200) {
                            arrTourDestinationList.clear()
                            arrTourDestinationList = it.data?.Data!!

                            if (arrTourDestinationList.size > 0) {
                                rvDestinationList.adapter =
                                    DestinationListAdapter(this, arrTourDestinationList, this)
                                swipeRefreshDestination.isRefreshing = false
                            }
                            showHideDesignView(1)
                            disposeProgress()
                        } else if (it.data?.Status == 1010 || it.data?.Status == 201) {
                            arrTourDestinationList?.clear()
                            rvDestinationList.adapter?.notifyDataSetChanged()
                            swipeRefreshDestination.isRefreshing = false
                            disposeProgress()
                            showHideDesignView(2)
                        }
                    } else {
                        disposeProgress()
                        it.message.toast(this)
                    }
                })
        } else {

            repo.getTourDestinationList(
                redionId,
                RoomTypeFilters,
                Himalayatrek,
                budgetfilter,
                DurationFilters,
                SpecialityFilters,
                OrderBy,
                IsSearch,
                sectorURL,
                PageIndex,
                PageSize,
                listners = ResponseListner {

                    if (it.status) {
                        if (it.data?.Status == 200) {
                            arrTourDestinationList.clear()
                            arrTourDestinationList = it.data?.Data!!

                            if (arrTourDestinationList.size > 0) {
                                rvDestinationList.adapter =
                                    DestinationListAdapter(this, arrTourDestinationList, this)
                                swipeRefreshDestination.isRefreshing = false
                            }
                            showHideDesignView(1)
                            disposeProgress()
                        } else if (it.data?.Status == 1010 || it.data?.Status == 201) {
                            arrTourDestinationList?.clear()
                            rvDestinationList.adapter?.notifyDataSetChanged()
                            swipeRefreshDestination.isRefreshing = false
                            disposeProgress()
                            showHideDesignView(2)
                        }
                    } else {
                        disposeProgress()
                        it.message.toast(this)
                    }
                })
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
                rvDestinationList.visible()
                rlMainNoData.gone()
            }
            2 -> {

                tvNoData.text = "No Data Found"
                imgNoData.setImageResource(R.drawable.tour_icon)

                rvDestinationList.gone()
                rlMainNoData.visible()
                tvRetry.gone()
            }
            3 -> {
                tvNoData.text = getString(R.string.msg_no_internet)
                imgNoData.setImageResource(R.drawable.ic_no_internet)

                rvDestinationList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
            4 -> {
                tvNoData.text = getString(R.string.msg_oops_something_went_wrong)
                imgNoData.setImageResource(R.drawable.ic_oops)

                rvDestinationList.gone()
                rlMainNoData.visible()
                tvRetry.visible()
            }
        }
    }
}