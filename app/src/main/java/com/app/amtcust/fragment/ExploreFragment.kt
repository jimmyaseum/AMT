package com.app.amtcust.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.activity.DestinationListActivity
import com.app.amtcust.adapter.ExplorePageAdapter.*
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.*
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.toast
import kotlinx.android.synthetic.main.fragment_explore.view.*

class ExploreFragment : BaseFragment(), View.OnClickListener, RecyclerClickListener {

    private var views: View? = null
    private val repo by lazy { NetworkRepo() }

    lateinit var adapterIndian: TopIndianDestinationAdapter
    private var arrIndianList: ArrayList<TopIndiaListModel> = ArrayList()

    lateinit var adapterTrend: TopTrendingHolidayDestinationAdapter
    private var arrTrendList: ArrayList<TopTrendListModel> = ArrayList()

    lateinit var adapterTour: ToursAdapter
    private var arrTournList: ArrayList<TourListModel> = ArrayList()

    lateinit var adapterCustomize: CustomizedHolidaysAdapter
    private var arrCustomizeList: ArrayList<CustomizedListModel> = ArrayList()

    lateinit var adapterHimalayan: HimalayanTreksAdapter
    private var arrHimalayanList: ArrayList<HimalayanListModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_explore, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        val layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        views!!.rvTopIndianDestinations.layoutManager = layoutManager

        val layoutManager1 = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        views!!.rvTopTrendingHolidayDestinations.layoutManager = layoutManager1

        val layoutManager2 = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        views!!.rvTours.layoutManager = layoutManager2

        val layoutManager4 = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        views!!.rvHimalayanTreks.layoutManager = layoutManager4

        val layoutManager3 = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        views!!.rvCustomizedHolidays.layoutManager = layoutManager3

        val layoutManager5 = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        views!!.rvParadiseOnEarth.layoutManager = layoutManager5

        GetTopIndianList()
        GetTopTrendingHolidayList()
        GetToursList()
        GetHimalayanList()
        GetCustomizedHolidaysList()

        views!!.rvParadiseOnEarth.adapter = ParadiseOnEarthAdapter(activity, this)

        initListeners()
    }

    private fun GetTopIndianList() {
        repo.getTopIndianList(listners = ResponseListner {
            if (it.status) {
                if (it.data?.Status == 200) {
                    arrIndianList.clear()
                    arrIndianList = it.data?.Data!!

                    if(arrIndianList.size > 0) {
                        adapterIndian = TopIndianDestinationAdapter(activity,arrIndianList,this)
                        views!!.rvTopIndianDestinations.adapter = adapterIndian
                    }

                } else
                    it.data?.Message?.toast(activity!!)
            } else
                it.message.toast(activity!!)

        })
    }

    private fun GetTopTrendingHolidayList() {

        repo.getTopTrendingHolidayList(listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrTrendList.clear()
                    arrTrendList = it.data?.Data!!

                    if(arrTrendList.size > 0) {
                        adapterTrend = TopTrendingHolidayDestinationAdapter(activity,arrTrendList,this)
                        views!!.rvTopTrendingHolidayDestinations.adapter = adapterTrend
                    }
                } else
                    it.data?.Message?.toast(activity!!)
            } else
                it.message.toast(activity!!)

        })
    }

    private fun GetToursList() {
        repo.getToursList(listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrTournList.clear()
                    arrTournList = it.data?.Data!!

                    if(arrTournList.size > 0) {
                        adapterTour = ToursAdapter(activity,arrTournList,this)
                        views!!.rvTours.adapter = adapterTour
                    }
                } else
                    it.data?.Message?.toast(activity!!)
            } else
                it.message.toast(activity!!)

        })
    }

    private fun GetCustomizedHolidaysList() {

        repo.getCustomizedHolidaysList(listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrCustomizeList.clear()
                    arrCustomizeList = it.data?.Data!!

                    if(arrCustomizeList.size > 0) {

                        adapterCustomize =  CustomizedHolidaysAdapter(activity,arrCustomizeList,this)
                        views!!.rvCustomizedHolidays.adapter = adapterCustomize

                    }
                } else
                    it.data?.Message?.toast(activity!!)
            } else
                it.message.toast(activity!!)

        })
    }


    private fun GetHimalayanList() {
        repo.getHimalayanList(listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrHimalayanList.clear()
                    arrHimalayanList = it.data?.Data!!

                    if(arrHimalayanList.size > 0) {
                        adapterHimalayan = HimalayanTreksAdapter(activity,arrHimalayanList,this)
                        views!!.rvHimalayanTreks.adapter = adapterHimalayan

                    }
                } else
                    it.data?.Message?.toast(activity!!)
            } else
                it.message.toast(activity!!)

        })
    }

    private fun initListeners() {
        views!!.tvViewAllTopIndianDestinations.setOnClickListener(this)
        views!!.tvViewAllTopTrendingHolidayDestinations.setOnClickListener(this)
        views!!.tvViewAllTours.setOnClickListener(this)
        views!!.tvViewAllHimalayanTreks.setOnClickListener(this)
        views!!.tvViewAllCustomizedHolidays.setOnClickListener(this)
        views!!.tvViewAllParadiseOnEarth.setOnClickListener(this)

        views!!.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE  || actionId == EditorInfo.IME_ACTION_SEARCH) {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","")
                intent.putExtra("SECTORID","")
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH",views!!.etSearch.text.toString())
                startActivity(intent)
                true
            } else {
                false
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.tvViewAllTopIndianDestinations -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","1")
                intent.putExtra("TRAVELTYPE","")
                intent.putExtra("SECTORID","")
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            R.id.tvViewAllTopTrendingHolidayDestinations -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","2")
                intent.putExtra("TRAVELTYPE","")
                intent.putExtra("SECTORID","")
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            R.id.tvViewAllTours -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","Tour")
                intent.putExtra("SECTORID","")
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            R.id.tvViewAllCustomizedHolidays -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","Package")
                intent.putExtra("SECTORID","")
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            R.id.tvViewAllHimalayanTreks -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","")
                intent.putExtra("SECTORID","")
                intent.putExtra("HIMALAYATREK","true")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            R.id.tvViewAllParadiseOnEarth -> {

            }
        }
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when (type) {
            101 -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","1")
                intent.putExtra("TRAVELTYPE","")
                intent.putExtra("SECTORID",arrIndianList[position].ID.toString())
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            102 -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","2")
                intent.putExtra("TRAVELTYPE","")
                intent.putExtra("SECTORID",arrTrendList[position].ID.toString())
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            103 -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","Tour")
                intent.putExtra("SECTORID",arrTournList[position].SectorID.toString())
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            104 -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","Package")
                intent.putExtra("SECTORID",arrCustomizeList[position].SectorID.toString())
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            105 -> {
                val intent = Intent(activity, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","")
                intent.putExtra("SECTORID",arrHimalayanList[position].SectorID.toString())
                intent.putExtra("HIMALAYATREK","true")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
            106 -> {

            }

        }
    }

}