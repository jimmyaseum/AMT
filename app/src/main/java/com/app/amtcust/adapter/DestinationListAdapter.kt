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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RecyclerClickListener {

        var context: Context? = null
        var hotelP: ArrayList<HotelRoom>? = null

        lateinit var adapterHotel: RoomTypeAdapter
        lateinit var adapterFacility: FacilityAdapter
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

            hotelP = model.hotelroom
            if(hotelP != null) {
                if(hotelP!!.size > 0) {
                    adapterHotel = RoomTypeAdapter(context, hotelP!!, this)
                    itemView.rvHotelData.adapter  = adapterHotel

                    var minimum: Int = hotelP!![0].Rate!!.toDouble().toInt()
                    var pos = 0
                    for (i in 1 until hotelP!!.size-1) {
                        if (minimum > hotelP!!.get(i).Rate!!.toDouble().toInt()) {
                             minimum = hotelP!!.get(i).Rate!!.toDouble().toInt()
                                pos = i
                        }
                    }
                    adapterHotel.updateItemSingle(pos)

                    val indiaLocale = Locale("en", "IN")
                    val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
                    val amount = india.format(hotelP!![pos].Rate!!.toBigDecimal())

                    itemView.txtRate.text = amount + " /-"
                }
            }

            var selectedcity = ""
            val citiesName: ArrayList<String> = ArrayList()
            val citiesP = model.toucities
            if(citiesP != null) {
                for(i in 0 until citiesP.size) {
                    if(i < 3) {
                        citiesName!!.add(citiesP[i].PlaceName!!)
                    }
                }
                selectedcity = TextUtils.join(", ", citiesName)

                itemView.txtCities.text = selectedcity

                if(model.Cities!! > 3) {
                    val more = model.Cities!! - 3
                    itemView.txtCitiesCount.text = "+ " + more
                }
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

        override fun onItemClickEvent(view: View, position: Int, type: Int) {

            if(type == 1008) {
                adapterHotel.updateItemSingle(position)

                val indiaLocale = Locale("en", "IN")
                val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)
                val amount = india.format(hotelP!![position].Rate!!.toBigDecimal())

                itemView.txtRate.text = amount + " /-"

            }
        }

    }
}
