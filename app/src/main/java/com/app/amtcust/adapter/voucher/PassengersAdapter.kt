package com.app.amtcust.adapter.voucher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.Passenger
import kotlinx.android.synthetic.main.adapter_passenger.view.*


class PassengersAdapter(val context: Context?, private val arrData: ArrayList<Passenger>) : RecyclerView.Adapter<PassengersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_passenger, parent, false)
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
            model: ArrayList<Passenger>
        ) {
            this.context = context

            if(model[position].CustomerName != null && model[position].CustomerName != "") {
                itemView.txtONPassenger.text = model[position].CustomerName
            }
            if(model[position].FlightNo != null && model[position].FlightNo != "") {
                itemView.txtONFlight.text = model[position].FlightNo
            }
            if(model[position].Class != null && model[position].Class != "") {
                itemView.txtONClass.text = model[position].Class
            }

        }
    }
}


