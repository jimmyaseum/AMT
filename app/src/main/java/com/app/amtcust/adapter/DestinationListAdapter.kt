package com.app.amtcust.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.Facility
import com.app.amtcust.model.response.HotelRoom
import com.app.amtcust.model.response.TourDestinationListModel
import com.app.amtcust.utils.gone
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_destination_list.view.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class DestinationListAdapter(val context: Context?, private val arrData: ArrayList<TourDestinationListModel>,
                             val recyclerClickListener: RecyclerClickListener) :
    RecyclerView.Adapter<DestinationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_destination_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, arrData[position], recyclerClickListener)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null
        var hotelP: ArrayList<HotelRoom>? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: TourDestinationListModel,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context


            if( model.TourImage != null && model.TourImage != "") {
                itemView.img.loadUrlRoundedCorner2(
                    model.TourImage,
                    R.drawable.no_image,
                    5
                )
            } else {
                itemView.img.loadUrlRoundedCorner2(
                    "",
                    R.drawable.no_image_placeholder,
                    5
                )
            }

            if(model.TourName != null) {
                itemView.txtDestinationName.text = model.TourName
            }

            if(model.Ratetype != null) {
                itemView.txtRateType.text = model.Ratetype
            }

            if(model.NoOfDays != null) {
                itemView.txtDays.text = model.NoOfDays.toString()
            }

            if(model.NoOfNights != null) {
                itemView.txtNights.text = model.NoOfNights.toString()
            }

            if(model.Ratetype != null) {
                itemView.txtRateType.text = model.Ratetype
            }

            if(model.Rate != null) {
                val indiaLocale = Locale("en", "IN")
                val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
                val amount = india.format(model.Rate?.toBigDecimal() ?: BigDecimal.ZERO)

                itemView.txtRate.text = amount + " /-"
            }

            if(model.RoomType != null) {
                itemView.txtRoomType.text = model.RoomType
            }

            if(model.TourCities != null) {
                itemView.txtCities.text = model.TourCities
            }

            if(model.toufacility.size > 0)
            {
                if(model.toufacility.size > 0) {
                    val adapterFacility = FacilityAdapter(context, model.toufacility)
                    itemView.rvFacilityData.adapter  = adapterFacility
                }
            } else {
                itemView.rvFacilityData.gone()
            }

            itemView.RlPlace.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 108)
            }

            itemView.txtViewDetails.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 109)
            }

            itemView.txt_customize.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 110)
            }

        }
    }
}
