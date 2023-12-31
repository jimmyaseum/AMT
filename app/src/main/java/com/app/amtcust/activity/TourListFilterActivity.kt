package com.app.amtcust.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.adapter.Filter.SpecialityFilterAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.SpecialityListModel
import com.app.amtcust.model.response.SpecialityResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.AppConstant
import com.app.amtcust.utils.hideKeyboard
import com.app.amtcust.utils.isOnline
import com.app.amtcust.utils.toast
import kotlinx.android.synthetic.main.activity_tour_filter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TourListFilterActivity : BaseActivity(), View.OnClickListener, RecyclerClickListener {

    var arrayListSpeciality: ArrayList<SpecialityListModel>? = null
    lateinit var adapterSpeciality: SpecialityFilterAdapter

    var SpecialityFilters = ""
    var DurationFilters = ""
    var BudgetFilters = ""

    var Count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_filter)
        initializeView()
    }

    override fun initializeView() {
        initListner()
    }

    private fun initListner() {

        imgBack.setOnClickListener(this)
        txtReset.setOnClickListener(this)
        cardbottom.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvFilterData.layoutManager = layoutManager

        setData()

    }

    override fun onClick(v: View?) {
        hideKeyboard(this, v)
        when (v?.id) {
            R.id.imgBack -> {
                finish()
            }
            R.id.txtReset -> {
                clearFilterData()
            }
            R.id.cardbottom -> {
                applyFiltersData()
            }
        }
    }

    private fun setData() {
        if (intent.hasExtra(AppConstant.SpecialityFilters)) {
            SpecialityFilters = intent.getStringExtra(AppConstant.SpecialityFilters).toString()
        }

        if (intent.hasExtra(AppConstant.DurationFilters)) {
            DurationFilters = intent.getStringExtra(AppConstant.DurationFilters).toString()
        }

        if (intent.hasExtra(AppConstant.BudgetFilters)) {
            BudgetFilters = intent.getStringExtra(AppConstant.BudgetFilters).toString()
        }

        if (isOnline(this)) {
            callGetSpecilityTypeAPI()
        } else {
            toast(getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
        }

        if (DurationFilters.isNotEmpty()) {
            cbDuration1.isChecked = false
            cbDuration2.isChecked = false
            cbDuration3.isChecked = false
            cbDuration4.isChecked = false
            cbDuration5.isChecked = false
            val splitList = DurationFilters.split(",")
            for (j in splitList.indices) {
                if (splitList[j].trim().equals("(NoOfDays BETWEEN 1 AND 3)")) {
                    cbDuration1.isChecked = true
                }
                if (splitList[j].trim().equals("(NoOfDays BETWEEN 4 AND 6)")) {
                    cbDuration2.isChecked = true
                }
                if (splitList[j].trim().equals("(NoOfDays BETWEEN 7 AND 9)")) {
                    cbDuration3.isChecked = true
                }
                if (splitList[j].trim().equals("(NoOfDays BETWEEN 10 AND 12)")) {
                    cbDuration4.isChecked = true
                }
                if (splitList[j].trim().equals("(NoOfDays > 12)")) {
                    cbDuration5.isChecked = true
                }
            }
        }

        if (BudgetFilters.isNotEmpty()) {
            cbBudget1.isChecked = false
            cbBudget2.isChecked = false
            cbBudget3.isChecked = false
            cbBudget4.isChecked = false
            cbBudget5.isChecked = false
            cbBudget6.isChecked = false
            val splitList = BudgetFilters.split(",")
            for (j in splitList.indices) {
                if (splitList[j].trim().equals("(Rate BETWEEN 0 AND 9999)")) {
                    cbBudget1.isChecked = true
                }
                if (splitList[j].trim().equals("(Rate BETWEEN 10000 AND 19999)")) {
                    cbBudget2.isChecked = true
                }
                if (splitList[j].trim().equals("(Rate BETWEEN 20000 AND 29999)")) {
                    cbBudget3.isChecked = true
                }
                if (splitList[j].trim().equals("(Rate BETWEEN 40000 AND 59999)")) {
                    cbBudget4.isChecked = true
                }
                if (splitList[j].trim().equals("(Rate BETWEEN 100000 AND 199999)")) {
                    cbBudget5.isChecked = true
                }
                if (splitList[j].trim().equals("(Rate > 200000)")) {
                    cbBudget6.isChecked = true
                }
            }
        }
    }

    /*API Call
    * Get all branch or center by employee wise
    * */
    private fun callGetSpecilityTypeAPI() {

        showProgress()

        val call = ApiUtils.apiInterface.getSpecialityType()

        call.enqueue(object : Callback<SpecialityResponse> {
            override fun onResponse(call: Call<SpecialityResponse>, response: Response<SpecialityResponse>) {

                if (response.code() == 200) {

                    if (response.body()?.Status == 200) {
                        hideProgress()
                        arrayListSpeciality = response.body()?.Data!!

                        Log.e("SpecialityFilters","==>"+SpecialityFilters)
                        //Selected position true if id contains in "branchFilters" as comma separated
                        if (SpecialityFilters.isNotEmpty()) {
                            for (i in arrayListSpeciality!!.indices) {

                                arrayListSpeciality!![i].isSelected = false
                                val splitList = SpecialityFilters.split(",")
                                for (j in splitList.indices) {
                                    if (arrayListSpeciality!![i].ID.toString() == splitList[j].trim()) {
                                        arrayListSpeciality!![i].isSelected = true
                                    }
                                }
                            }
                        }
                        adapterSpeciality = SpecialityFilterAdapter(arrayListSpeciality, this@TourListFilterActivity)
                        rvFilterData.adapter = adapterSpeciality
                    } else {
                        hideProgress()
                        toast(response.body()?.Message.toString(), AppConstant.TOAST_SHORT)
                    }
                }
            }

            override fun onFailure(call: Call<SpecialityResponse>, t: Throwable) {
                hideProgress()
                toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when (view.id) {
            R.id.llContent -> {
                if (type == 1001) {
                    adapterSpeciality.updateItem(position)

                }
            }
        }
    }

    private fun clearFilterData() {

        if (::adapterSpeciality.isInitialized) {
            adapterSpeciality.clearSelectedItems()
        } else {
            if (!arrayListSpeciality.isNullOrEmpty()) {
                for (model in arrayListSpeciality as ArrayList) {
                    model.isSelected = false
                }
            }
        }

        cbBudget1.isChecked = false
        cbBudget2.isChecked = false
        cbBudget3.isChecked = false
        cbBudget4.isChecked = false
        cbBudget5.isChecked = false
        cbBudget6.isChecked = false

        cbDuration1.isChecked = false
        cbDuration2.isChecked = false
        cbDuration3.isChecked = false
        cbDuration4.isChecked = false
        cbDuration5.isChecked = false


    }

    private fun applyFiltersData() {

        var totalfilter1 = 0
        var totalfilter2 = 0
        var totalfilter3 = 0
        val arrayListSpecialityFilter: ArrayList<String> = ArrayList()
        if (!arrayListSpeciality.isNullOrEmpty()) {
            for (i in 0 until arrayListSpeciality!!.size) {
                if (arrayListSpeciality!![i].isSelected) {
                    arrayListSpecialityFilter.add(arrayListSpeciality!![i].ID.toString())
                }
            }
            SpecialityFilters = arrayListSpecialityFilter.toString().replace("[", "").replace("]", "")

            totalfilter1 = arrayListSpecialityFilter.size
        }

        var arrayListDurationFilter: ArrayList<String>? = ArrayList()
        if(cbDuration1.isChecked) {
            arrayListDurationFilter!!.add("(NoOfDays BETWEEN 1 AND 3)")
        }
        if(cbDuration2.isChecked) {
            arrayListDurationFilter!!.add("(NoOfDays BETWEEN 4 AND 6)")
        }
        if(cbDuration3.isChecked) {
            arrayListDurationFilter!!.add("(NoOfDays BETWEEN 7 AND 9)")
        }
        if(cbDuration4.isChecked) {
            arrayListDurationFilter!!.add("(NoOfDays BETWEEN 10 AND 12)")
        }
        if(cbDuration5.isChecked) {
            arrayListDurationFilter!!.add("(NoOfDays > 12)")
        }

        if(!arrayListDurationFilter.isNullOrEmpty()) {
            DurationFilters = arrayListDurationFilter.toString().replace("[", "").replace("]", "")
            totalfilter2 = arrayListDurationFilter.size
        } else {
            DurationFilters = ""
        }

        var arrayListBudgetFilter: ArrayList<String>? = ArrayList()
        if(cbBudget1.isChecked) {
            arrayListBudgetFilter!!.add("(Rate BETWEEN 0 AND 9999)")
        }
        if(cbBudget2.isChecked) {
            arrayListBudgetFilter!!.add("(Rate BETWEEN 10000 AND 19999)")
        }
        if(cbBudget3.isChecked) {
            arrayListBudgetFilter!!.add("(Rate BETWEEN 20000 AND 29999)")
        }
        if(cbBudget4.isChecked) {
            arrayListBudgetFilter!!.add("(Rate BETWEEN 40000 AND 59999)")
        }
        if(cbBudget5.isChecked) {
            arrayListBudgetFilter!!.add("(Rate BETWEEN 100000 AND 199999)")
        }
        if(cbBudget6.isChecked) {
            arrayListBudgetFilter!!.add("(Rate > 200000)")
        }
        if(!arrayListBudgetFilter.isNullOrEmpty()) {
            BudgetFilters = arrayListBudgetFilter.toString().replace("[", "").replace("]", "")
            totalfilter3 = arrayListBudgetFilter.size
        } else {
            BudgetFilters = ""
        }

        Count = totalfilter1 + totalfilter2 + totalfilter3

        val intent = Intent()
        intent.putExtra(AppConstant.SpecialityFilters, SpecialityFilters)
        intent.putExtra(AppConstant.DurationFilters, DurationFilters)
        intent.putExtra(AppConstant.BudgetFilters,BudgetFilters)
        intent.putExtra("Count",Count)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }
}