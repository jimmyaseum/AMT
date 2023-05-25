package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.HotelCostData
import kotlinx.android.synthetic.main.adapter_hotel_cost.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class HotelCostAdapter(val context: Context?, private val arrData: ArrayList<HotelCostData>) : RecyclerView.Adapter<HotelCostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_hotel_cost, parent, false)
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
            model: ArrayList<HotelCostData>
        ) {
            this.context = context

            val indiaLocale = Locale("en", "IN")
            val india: NumberFormat = NumberFormat.getCurrencyInstance(indiaLocale)

            if(model[position].RoomType != null) {
                itemView.txt_RoomType.text = model[position].RoomType
            }
            if(model[position].TwinSharingRate != null) {
                val amount = india.format(model[position].TwinSharingRate!!.toBigDecimal())
                itemView.txt_TwinSharingRate.text = amount + " /-"
            }
            if(model[position].ThreeSharingRate != null) {
                val amount = india.format(model[position].ThreeSharingRate!!.toBigDecimal())
                itemView.txt_ThreeSharingRate.text = amount + " /-"
            }
            if(model[position].FourSharingRate != null) {
                val amount = india.format(model[position].FourSharingRate!!.toBigDecimal())
                itemView.txt_FourSharingRate.text = amount + " /-"
            }
            if(model[position].CWBRate != null) {
                val amount = india.format(model[position].CWBRate!!.toBigDecimal())
                itemView.txt_CWBRate.text = amount + " /-"
            }
            if(model[position].CNBRate != null) {
                val amount = india.format(model[position].CNBRate!!.toBigDecimal())
                itemView.txt_CNBRate.text = amount + " /-"
            }
            if(model[position].ExtraAdultRate != null) {
                val amount = india.format(model[position].ExtraAdultRate!!.toBigDecimal())
                itemView.txt_ExtraAdultRate.text = amount + " /-"
            }
            if(model[position].InfantRate != null) {
                val amount = india.format(model[position].InfantRate!!.toBigDecimal())
                itemView.txt_InfantRate.text = amount + " /-"
            }
        }
    }
    // Single Filter Selection
    fun updateItemSingle(position: Int) {

        for (i in arrData?.indices!!) {
            if (arrData[i].isSelected) {
                arrData[i].isSelected = false
            }
        }
        arrData[position].isSelected = true
        notifyDataSetChanged()
    }


}


