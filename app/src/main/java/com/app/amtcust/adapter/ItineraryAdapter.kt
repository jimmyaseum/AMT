package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.ItineraryData
import com.app.amtcust.utils.gone
import com.app.amtcust.utils.isVisible
import com.app.amtcust.utils.visible
import kotlinx.android.synthetic.main.adapter_itinerary_included.view.*

class ItineraryAdapter(val context: Context?, private val arrData: ArrayList<ItineraryData>) : RecyclerView.Adapter<ItineraryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_itinerary_included, parent, false)
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
            model: ArrayList<ItineraryData>
        ) {
            this.context = context

            if(model[position].ItineraryDay != null) {
                itemView.txt_ItineraryDay.text = "Day "+ model[position].ItineraryDay.toString()
            }
            if(model[position].ItineraryTitle != null) {
                itemView.txt_ItineraryTitle.text = model[position].ItineraryTitle
            }
            if(model[position].ItineraryDescription != null) {
                itemView.txt_ItineraryDescription.text = model[position].ItineraryDescription
            }

            if(position == 0) {
                itemView.v1.setBackgroundColor(context.resources.getColor(R.color.colorWhite))
            } else {
                itemView.v1.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
            }

            if(position == model.size-1) {
                itemView.v2.setBackgroundColor(context.resources.getColor(R.color.colorWhite))
            } else {
                itemView.v2.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
            }
            if(model[position].tourfcs != null) {
                val FacilityP = model[position].tourfcs
                if(FacilityP != null) {
                    if(FacilityP!!.size > 0) {
                        itemView.rvItineraryFacilityData.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        val adapterFacility = IineraryFacilityAdapter(context, FacilityP!!)
                        itemView.rvItineraryFacilityData.adapter  = adapterFacility
                    }
                }
            }

            itemView.setOnClickListener {

                if(!itemView.txt_ItineraryDescription.isVisible()) {
                    itemView.txt_ItineraryDescription.visible()
                    itemView.imgExpand.setImageResource(R.drawable.ic_keyboard_up)
                    itemView.imgfill.setImageResource(R.drawable.ic_circle)


                } else {
                    itemView.txt_ItineraryDescription.gone()
                    itemView.imgExpand.setImageResource(R.drawable.ic_keyboard_down)
                    itemView.imgfill.setImageResource(R.drawable.ic_circle_unfill)
                }
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


