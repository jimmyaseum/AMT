package com.app.amtcust.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.activity.TourBookingFormActivity
import com.app.amtcust.model.response.TourBookingModel
import com.app.amtcust.utils.*
import kotlinx.android.synthetic.main.adapter_competed_tours.view.*
import kotlinx.android.synthetic.main.adapter_competed_tours.view.img
import kotlinx.android.synthetic.main.adapter_competed_tours.view.txtDays
import java.util.*

class UpcomingTourListAdapter(val context: Context?, private val arrData: ArrayList<TourBookingModel>) : RecyclerView.Adapter<UpcomingTourListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_competed_tours, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position,arrData)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<TourBookingModel>
        ) {
            this.context = context

            if(model[position].TourName != null) {
                itemView.txtTourName.text = model[position].TourName
            }

            if(model[position].TourImage != null && model[position].TourImage != "" ) {
                itemView.img.loadUrlRoundedCorner2(
                    model[position].TourImage,
                    R.drawable.no_image,
                    1
                )
            } else {
                itemView.img.loadUrlRoundedCorner2(
                    "",
                    R.drawable.no_image_placeholder,
                    1
                )
            }

            val calendar1: Calendar = Calendar.getInstance()
            val startdate = convertDateToString(calendar1.time, AppConstant.dd_MM_yyyy_Slash)

            val calendar2: Calendar = Calendar.getInstance()
            calendar2.add(Calendar.DATE, model[position].TourStartDate!!.toInt())
            val returnDate = convertDateToString(calendar2.time, AppConstant.dd_MM_yyyy_Slash)

            var days = getFormattedMonth(startdate, returnDate, AppConstant.dd_MM_yyyy_Slash)

            if(days.contains(",")) {
                var split = days.split(",")

            }

            itemView.txtDays.text = days + " to go"

            itemView.LLPlaces.setOnClickListener {
//                recyclerClickListener.onItemClickEvent(it, position, 110)
                val intent = Intent(context, TourBookingFormActivity::class.java)
                intent.putExtra("ID",model[position].ID)
                context.startActivity(intent)
            }

        }
    }
}
