package com.app.amtcust.adapter.voucher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.UpcomingHotelModel
import com.app.amtcust.utils.AppConstant
import com.app.amtcust.utils.convertDateStringToString
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_history_hotel_list.view.*
import kotlinx.android.synthetic.main.adapter_history_hotel_list.view.img
import kotlinx.android.synthetic.main.adapter_history_hotel_list.view.llStartView

class HistoryHotelListAdapter(val context: Context?, private val arrData: ArrayList<UpcomingHotelModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<HistoryHotelListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_history_hotel_list, parent, false)
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
            model: ArrayList<UpcomingHotelModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].HotelName != null) {
                itemView.txtHotelName.text = model[position].HotelName
            }

            if(model[position].TotalNoOfRooms != null) {
                itemView.txtTotalNoOfRooms.text = "" + model[position].TotalNoOfRooms + " Rooms"
            }
            if(model[position].TotalNoOfPax != null) {
                itemView.txtTotalNoOfPax.text = ", "+ model[position].TotalNoOfPax + " Adults"
            }

            if(model[position].CheckinDate != null) {
                val departureDate = convertDateStringToString(model[position].CheckinDate!!, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM_YYYY)!!
                itemView.txtCheckInDate.text = departureDate
            }

            if(model[position].CheckoutDate != null) {
                val departureDate = convertDateStringToString(model[position].CheckoutDate!!, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM_YYYY)!!
                itemView.txtCheckOutDate.text = departureDate
            }

            if( model[position].HotelImage != null) {
                itemView.img.loadUrlRoundedCorner2(
                    model[position].HotelImage ,
                    R.drawable.no_image,
                    5
                )
            }

            itemView.llStartView.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 107)
            }
        }
    }
}
