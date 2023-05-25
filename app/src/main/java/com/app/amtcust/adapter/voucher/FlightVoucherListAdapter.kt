package com.app.amtcust.adapter.voucher

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.FlightVoucherModel
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.adapter_flight_voucher_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class FlightVoucherListAdapter(val context: Context?, private val arrData: ArrayList<FlightVoucherModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<FlightVoucherListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_flight_voucher_list, parent, false)
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
        var colors = arrayOf("#cceff2", "#F6F6EB", "#F6ECF5")

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<FlightVoucherModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].IsCreatedFromApp!!) {
                itemView.card2.visible()
                itemView.card1.gone()

                if(model[position].AirlinevoucherNo != null) {
                    itemView.txtAVN.text = model[position].AirlinevoucherNo
                }
                if(model[position].TourBookingNo != null) {
                    itemView.txtTourBookingNo.text = model[position].TourBookingNo
                }
                if(model[position].NoOfPax != null) {
                    itemView.txtNoOfPax.text = model[position].NoOfPax.toString()
                }
                if(model[position].TotalPrice != null) {
                    itemView.txtTotalPrice.text = model[position].TotalPrice.toString()
                }
                if(model[position].AirlineVoucherTicket != null && model[position].AirlineVoucherTicket != "") {
                    itemView.txtAirlineDocument.setOnClickListener {
                        if(isOnline(context)) {
                            if(model[position].AirlineVoucherTicket!!.contains(".pdf")) {
                                var format = "https://docs.google.com/gview?embedded=true&url=%s"
                                val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, model[position].AirlineVoucherTicket)
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                                context.startActivity(browserIntent)
                            } else {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model[position].AirlineVoucherTicket))
                                context.startActivity(browserIntent)
                            }
                        } else {
                            context.toast(context.resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                        }
                    }
                } else {
                    itemView.txtAirlineDocument.gone()
                }

                if(model[position].DeparturePNRNo != null && model[position].DeparturePNRNo != "") {
                    itemView.llDeparturePNR.visible()
                    itemView.txtDeparturePNR.text = model[position].DeparturePNRNo
                } else {
                    itemView.llDeparturePNR.gone()
                }

                if(model[position].ArrivalPNRNo != null && model[position].ArrivalPNRNo != "") {
                    itemView.llReturnPNR.visible()
                    itemView.txtReturnPNR.text = model[position].ArrivalPNRNo
                } else {
                    itemView.llReturnPNR.gone()
                }

                if(model[position].TicketPurchasedDate != null && model[position].TicketPurchasedDate != "") {
                    itemView.txtPurchaseDate.text = model[position].TicketPurchasedDate
                }

                val reminder = position % 3
                itemView.llStartView2.setBackgroundColor(Color.parseColor(colors[reminder]))

            } else {

                itemView.card1.visible()
                itemView.card2.gone()

                if(model[position].FlightNo != null) {
                    itemView.txtFlightNo.text = model[position].FlightNo
                }
                if(model[position].PNRNo != null) {
                    itemView.txtPNRNo.text = model[position].PNRNo
                }

                if(model[position].FromAirport != null) {
                    itemView.txtFromAirportName.text = model[position].FromAirport
                }
                if(model[position].ToAirport != null) {
                    itemView.txtToAirportName.text = model[position].ToAirport
                }

                if(model[position].FromAirportCode != null) {
                    itemView.txtFromAirportCode.text = model[position].FromAirportCode
                }
                if(model[position].ToAirportCode != null) {
                    itemView.txtToAirportCode.text = model[position].ToAirportCode
                }

                if(model[position].DepartureTime != null) {
                    val departureTime = convertDateStringToString(model[position].DepartureTime!!, AppConstant.HH_MM_FORMAT, AppConstant.HH_MM_AA_FORMAT)!!
                    itemView.txtDeparTime.text = departureTime
                }
                if(model[position].ArrivalTime != null) {
                    val arrivalTime = convertDateStringToString(model[position].ArrivalTime!!, AppConstant.HH_MM_FORMAT, AppConstant.HH_MM_AA_FORMAT)!!
                    itemView.txtArriveTime.text = arrivalTime
                }

                if(model[position].DepartureDate != null) {
                    val departureDate = convertDateStringToString(model[position].DepartureDate!!, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM)!!
                    itemView.txtDeparDate.text = departureDate
                }
                if(model[position].ArrivalDate != null) {
                    val arrivalDate = convertDateStringToString(model[position].ArrivalDate!!, AppConstant.dd_MM_yyyy_Slash, AppConstant.day_d_MM)!!
                    itemView.txtArriveDate.text = arrivalDate
                }

                val reminder = position % 3
                itemView.llStartView.setBackgroundColor(Color.parseColor(colors[reminder]))


                itemView.llStartView.setOnClickListener {
                    recyclerClickListener.onItemClickEvent(it, position, 108)
                }
            }

        }
    }
}
