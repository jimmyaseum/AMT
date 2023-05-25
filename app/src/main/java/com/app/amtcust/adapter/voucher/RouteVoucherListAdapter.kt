package com.app.amtcust.adapter.voucher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.RouteVoucherModel
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_route_voucher_list.view.*

class RouteVoucherListAdapter(val context: Context?, private val arrData: ArrayList<RouteVoucherModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<RouteVoucherListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_route_voucher_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, arrData, recyclerClickListener)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<RouteVoucherModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].TourBookingNo != null) {
                itemView.txtBookingNo.text = "Booking No : " +model[position].TourBookingNo
            }

            if(model[position].VehicleType != null) {
                itemView.txtVehicleType.text = model[position].VehicleType
            }
            if(model[position].NoOfPax != null) {
                itemView.txtNoofPassangers.text = ""+ model[position].NoOfPax +" Passengers"
            }

            if( model[position].VehicleImage != null) {
                itemView.img.loadUrlRoundedCorner2(
                    model[position].VehicleImage ,
                    R.drawable.no_image,
                    5
                )
            }

            itemView.llStartView.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 109)
            }
        }
    }
}
