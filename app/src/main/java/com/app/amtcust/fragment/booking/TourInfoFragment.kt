package com.app.amtcust.fragment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.amtcust.R
import com.app.amtcust.activity.TourBookingFormActivity
import com.app.amtcust.fragment.BaseFragment
import com.app.amtcust.model.response.TourInformationModel
import com.app.amtcust.model.response.TourInformationResponse
import com.app.amtcust.retrofit.ApiUtils
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.fragment_tour_info.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TourInfoFragment  : BaseFragment()  {
    private var views: View? = null
    var sharedPreference: SharedPreference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_tour_info, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {
        sharedPreference = SharedPreference(activity!!)
        val userId = sharedPreference?.getPreferenceInt(PrefConstants.PREF_TOUR_BOOKING_ID)!!
        callDetailApi(userId)

        views!!.LLcardButtonNext.setOnClickListener {
            var tourFormActivity: TourBookingFormActivity = activity as TourBookingFormActivity
            tourFormActivity.updateStepsColor(1)
            tourFormActivity.CURRENT_STEP_POSITION = 1

            val fragment = PersonalInfoFragment()
            replaceFragment(R.id.container, fragment, PersonalInfoFragment::class.java.simpleName)

        }

    }

    private fun callDetailApi(tourbookingid: Int) {

        showProgress()
        var jsonObject = JSONObject()
        jsonObject.put("ID", tourbookingid)

        val call = ApiUtils.apiInterface.getTourInformation(getRequestJSONBody(jsonObject.toString()))

        call.enqueue(object : Callback<TourInformationResponse> {
            override fun onResponse(call: Call<TourInformationResponse>, response: Response<TourInformationResponse>) {
                if (response.code() == 200) {
                    if (response.body()?.Status == 200) {
                        val arrayList = response.body()?.Data!!
                        if(arrayList.size > 0) {
                            setAPIData(arrayList)
                        }
                    }
                    hideProgress()
                }
            }
            override fun onFailure(call: Call<TourInformationResponse>, t: Throwable) {
                hideProgress()
                activity!!.toast(getString(R.string.error_failed_to_connect), AppConstant.TOAST_SHORT)
            }
        })
    }

    private fun setAPIData(arrayList: ArrayList<TourInformationModel>) {

        if(arrayList[0].SectorName != null && arrayList[0].SectorName != "") {
            views!!.edtSector.setText(arrayList[0].SectorName)
        }

        if(arrayList[0].SubSectorName != null && arrayList[0].SubSectorName != "" ) {
            views!!.edtSubSector.setText(arrayList[0].SubSectorName)
        } else {
            views!!.ll_sub_sector.gone()
        }
        if(arrayList[0].PBN != null && arrayList[0].PBN != "") {
            views!!.edtTourBookingNo.setText(arrayList[0].PBN)
        }

        if(arrayList[0].TourName != null && arrayList[0].TourName != "") {
            views!!.edtTourName.setText(arrayList[0].TourName)
        }
        if(arrayList[0].TourStartDate != null && arrayList[0].TourStartDate != "") {
            views!!.edtTourStartDate.setText(arrayList[0].TourStartDate)
        }
        if(arrayList[0].TourEndDate != null && arrayList[0].TourEndDate != "") {
            views!!.edtTourEndDate.setText(arrayList[0].TourEndDate)
        }
        if(arrayList[0].NoOfNights != null && arrayList[0].NoOfNights != 0) {
            views!!.edtNoN.setText(arrayList[0].NoOfNights.toString())
        }
        if(arrayList[0].RoomType != null && arrayList[0].RoomType != "") {
            views!!.edtRoomType.setText(arrayList[0].RoomType)
        }
        if(arrayList[0].VehicleSharing != null && arrayList[0].VehicleSharing != "") {
            views!!.edtVehicleSharing.setText(arrayList[0].VehicleSharing)
        }
        if(arrayList[0].TotalNoOfRooms != null && arrayList[0].TotalNoOfRooms != 0) {
            views!!.edtTotalRooms.setText(arrayList[0].TotalNoOfRooms.toString())
        }
        if(arrayList[0].TotalNoOfSeats != null && arrayList[0].TotalNoOfSeats != 0) {
            views!!.edtTotalSeats.setText(arrayList[0].TotalNoOfSeats.toString())
        }
    }
}