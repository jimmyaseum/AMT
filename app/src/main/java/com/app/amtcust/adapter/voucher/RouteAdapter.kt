package com.app.amtcust.adapter.voucher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.RouteDetailsModel
import kotlinx.android.synthetic.main.adapter_route_detail.view.*


class RouteAdapter(val context: Context?, private val arrData: ArrayList<RouteDetailsModel>) : RecyclerView.Adapter<RouteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_route_detail, parent, false)
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
            model: ArrayList<RouteDetailsModel>
        ) {
            this.context = context

            itemView.day.text = "Day : " //+ (position + 1)

            if(model[position].DayNo != null && model[position].DayNo != 0) {
                itemView.txtDay.text = model[position].DayNo.toString()
            }
            if(model[position].Date != null && model[position].Date != "") {
                itemView.txtDate.text = model[position].Date
            }
            if(model[position].CityName != null && model[position].CityName != "") {
                itemView.txtPlace.text = model[position].CityName
            }
            if(model[position].HotelName != null && model[position].HotelName != "") {
                itemView.txtHotel.text = model[position].HotelName
            }
            if(model[position].Date != null && model[position].Date != "") {
                itemView.txtAddress.text = model[position].Date
            }
            if(model[position].Description != null && model[position].Description != "") {
                itemView.txtDescription.text = model[position].Description
            }



        }
    }
}


