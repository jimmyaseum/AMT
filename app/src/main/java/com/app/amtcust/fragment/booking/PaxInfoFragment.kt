package com.app.amtcust.fragment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtcust.R
import com.app.amtcust.adapter.AddMorePaxUpdate
import com.app.amtcust.fragment.BaseFragment
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.TourPaxInformationModel
import com.app.amtcust.model.response.TourPaxInformationResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.fragment_pax_info.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PaxInfoFragment   : BaseFragment(), RecyclerClickListener {
    private var views: View? = null
    private val repo by lazy { NetworkRepo() }
    var sharedPreference: SharedPreference? = null

    lateinit var adapter: AddMorePaxUpdate
    private var arrPaxList: ArrayList<TourPaxInformationModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_pax_info, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)
        val userId = sharedPreference?.getPreferenceInt(PrefConstants.PREF_TOUR_BOOKING_ID)!!
        callDetailApi(userId)

    }

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()
        var jsonObject = JSONObject()
        jsonObject.put("ID", tourbookingid)

        val call = ApiUtils.apiInterface.getTourPaxInformation(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourPaxInformationResponse> {
            override fun onResponse(call: Call<TourPaxInformationResponse>, response: Response<TourPaxInformationResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {

                        arrPaxList.clear()
                        arrPaxList = response.body()?.Data!!

                        if(arrPaxList.size > 0) {
                            setAdapterData(arrPaxList)
                        }
                    }

                }
            }
            override fun onFailure(call: Call<TourPaxInformationResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAdapterData(arrayList: ArrayList<TourPaxInformationModel>) {

        adapter = AddMorePaxUpdate(activity!!,arrayList, this)
        views!!.rvPaxInfo.adapter = adapter
        hideProgress()
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
    }

}