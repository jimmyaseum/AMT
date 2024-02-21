package com.app.amtcust.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.amtcust.R
import com.app.amtcust.activity.DestinationListActivity
import com.app.amtcust.adapter.WorldTourListAdapter
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.SectorListModel
import com.app.amtcust.retrofit.NetworkRepo
import com.app.amtcust.retrofit.ResponseListner
import com.app.amtcust.utils.disposeProgress
import com.app.amtcust.utils.launchProgress
import com.app.amtcust.utils.toast
import kotlinx.android.synthetic.main.fragment_world_tour.view.*

class WorldTourFragment : BaseFragment(), RecyclerClickListener {

    private var views: View? = null
    private val repo by lazy { NetworkRepo() }
    lateinit var adapter: WorldTourListAdapter
    private var arrSectorList: ArrayList<SectorListModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_world_tour, container, false)
        initializeView()
        return views
    }

    override fun initializeView() {

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        views!!.rvWorldTourList.layoutManager = staggeredGridLayoutManager
        GetSectorList()
    }

    private fun GetSectorList() {
        launchProgress()
        repo.getSectorList("international", listners = ResponseListner {

            if (it.status) {
                if (it.data?.Status == 200) {
                    arrSectorList.clear()
                    arrSectorList = it.data?.Data!!

                    if(arrSectorList.size > 0) {
                        views!!.rvWorldTourList.adapter = WorldTourListAdapter(activity,arrSectorList,this)
                    }
                    disposeProgress()
                } else {
                    disposeProgress()
                    it.data?.Message?.toast(activity!!)
                }
            } else {
                disposeProgress()
                it.message.toast(activity!!)
            }

        })
    }

    override fun onItemClickEvent(view: View, position: Int, type: Int) {
        when (type) {
            109 -> {
                val intent = Intent(activity!!, DestinationListActivity::class.java)
                intent.putExtra("REGIONID","")
                intent.putExtra("TRAVELTYPE","TOUR")
                intent.putExtra("SECTORURL",arrSectorList[position].DestinationURL.toString())
                intent.putExtra("HIMALAYATREK","")
                intent.putExtra("ISSEARCH","")
                startActivity(intent)
            }
        }
    }
}