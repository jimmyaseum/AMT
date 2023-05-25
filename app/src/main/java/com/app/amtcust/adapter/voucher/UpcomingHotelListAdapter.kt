package com.app.amtcust.adapter.voucher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.UpcomingHotelModel
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.adapter_upcoming_hotel_list.view.*
import kotlinx.android.synthetic.main.adapter_upcoming_hotel_list.view.img

class UpcomingHotelListAdapter(val context: Context?, private val arrData: ArrayList<UpcomingHotelModel>, val recyclerClickListener: RecyclerClickListener, val isshow: Boolean) : RecyclerView.Adapter<UpcomingHotelListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_upcoming_hotel_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, arrData, recyclerClickListener, isshow)
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
            recyclerClickListener: RecyclerClickListener,
            isshow: Boolean
        ) {
            this.context = context

            if(model[position].HotelName != null) {
                itemView.txtHotelName.text = model[position].HotelName
            }
            if(model[position].RoomType != null) {
                itemView.txtRoomType.text = model[position].RoomType
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

            if(isshow) {
                itemView.LLcardButtonDetails.visible()
                itemView.LLcardButtonDetails.setOnClickListener {
                    recyclerClickListener.onItemClickEvent(it, position, 107)
                }
            } else {
                itemView.LLcardButtonDetails.gone()
                itemView.setOnClickListener {
                    recyclerClickListener.onItemClickEvent(it, position, 107)
                }
            }
        }
    }
}
