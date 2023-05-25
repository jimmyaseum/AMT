package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.Facility
import kotlinx.android.synthetic.main.adapter_itinerary_facility_included.view.*

class IineraryFacilityAdapter(val context: Context?, private val arrData: ArrayList<Facility>) : RecyclerView.Adapter<IineraryFacilityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_itinerary_facility_included, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bindItems(context!!, position, arrData)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<Facility>
        ) {
            this.context = context

            if(model[position].Name != null) {
                itemView.txt_facility_name.text = model[position].Name
            }

        }
    }
    // Single Filter Selection
    fun updateItemSingle(position: Int) {

        for (i in arrData?.indices!!) {
            if (arrData[i].isSelected) {
                arrData[i].isSelected = false
            }
        }
        arrData[position].isSelected = true
        notifyDataSetChanged()
    }


}


