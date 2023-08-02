package com.app.amtcust.adapter.voucher

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import java.util.Locale

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
        var colors = arrayOf("#cceff2", "#F6F6EB", "#F6ECF5")

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<UpcomingHotelModel>,
            recyclerClickListener: RecyclerClickListener,
            isshow: Boolean
        ) {
            this.context = context

            if(model[position].IsCreatedFromApp!!) {
                itemView.card2.visible()
                itemView.card1.gone()

                if(model[position].HotelVoucher != null && model[position].HotelVoucher != "") {
                    itemView.txtHotelVoucher.text = model[position].HotelVoucher
                    itemView.llHotelVoucher.visible()
                } else {
                    itemView.llHotelVoucher.gone()
                }

                if(model[position].TourBookingNo != null && model[position].TourBookingNo != "") {
                    itemView.txtTourBookingNo.text = model[position].TourBookingNo
                    itemView.llTourBookingNo.visible()
                } else {
                    itemView.llTourBookingNo.gone()
                }

                if(model[position].TourName != null && model[position].TourName != "") {
                    itemView.txtTourName.text = model[position].TourName
                    itemView.llTourName.visible()
                } else {
                    itemView.llTourName.gone()
                }

                if(model[position].RoomType != null && model[position].RoomType != "") {
                    itemView.txtRoomType2.text = model[position].RoomType
                    itemView.llRoomType2.visible()
                } else {
                    itemView.llRoomType2.gone()
                }

                if(model[position].TotalNoOfRooms != null && model[position].TotalNoOfRooms != 0) {
                    itemView.llTotalNoOfRooms.visible()
                    itemView.txtTotalNoOfRooms2.text = model[position].TotalNoOfRooms.toString()
                } else {
                    itemView.llTotalNoOfRooms.gone()
                }

                if(model[position].TotalNoOfPax != null && model[position].TotalNoOfPax != 0) {
                    itemView.txtTotalNoOfPax2.text = model[position].TotalNoOfPax.toString()
                    itemView.llTotalNoOfPax.visible()
                } else {
                    itemView.llTotalNoOfPax.gone()
                }

                if(model[position].HotelVoucherImage != null && model[position].HotelVoucherImage != "") {
                    itemView.llHotelDocument.visible()
                    itemView.txtHotelDocument.setOnClickListener {
                        if(isOnline(context)) {
                            if(model[position].HotelVoucherImage!!.contains(".pdf")) {
                                var format = "https://docs.google.com/gview?embedded=true&url=%s"
                                val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, model[position].HotelVoucherImage)
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                                context.startActivity(browserIntent)
                            } else {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model[position].HotelVoucherImage))
                                context.startActivity(browserIntent)
                            }
                        } else {
                            context.toast(context.resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                        }
                    }
                } else {
                    itemView.llHotelDocument.gone()
                }

                val reminder = position % 3
                itemView.llStartView2.setBackgroundColor(Color.parseColor(colors[reminder]))

            }
            else {
                itemView.card1.visible()
                itemView.card2.gone()

                if (model[position].HotelName != null) {
                    itemView.txtHotelName.text = model[position].HotelName
                }
                if (model[position].RoomType != null) {
                    itemView.txtRoomType.text = model[position].RoomType
                }

                if (model[position].TotalNoOfRooms != null) {
                    itemView.txtTotalNoOfRooms.text = "" + model[position].TotalNoOfRooms + " Rooms"
                }
                if (model[position].TotalNoOfPax != null) {
                    itemView.txtTotalNoOfPax.text = ", " + model[position].TotalNoOfPax + " Adults"
                }

                if (model[position].CheckinDate != null) {
                    val departureDate = convertDateStringToString(
                        model[position].CheckinDate!!,
                        AppConstant.dd_MM_yyyy_Slash,
                        AppConstant.day_d_MM_YYYY
                    )!!
                    itemView.txtCheckInDate.text = departureDate
                }

                if (model[position].CheckoutDate != null) {
                    val departureDate = convertDateStringToString(
                        model[position].CheckoutDate!!,
                        AppConstant.dd_MM_yyyy_Slash,
                        AppConstant.day_d_MM_YYYY
                    )!!
                    itemView.txtCheckOutDate.text = departureDate
                }

                if (model[position].HotelImage != null) {
                    itemView.img.loadUrlRoundedCorner2(
                        model[position].HotelImage,
                        R.drawable.no_image,
                        5
                    )
                }

                if (isshow) {
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
}
