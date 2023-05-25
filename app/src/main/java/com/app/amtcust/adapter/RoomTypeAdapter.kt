package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.HotelRoom
import kotlinx.android.synthetic.main.adapter_hotel_included.view.*

class RoomTypeAdapter(val context: Context?, private val arrData: ArrayList<HotelRoom>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<RoomTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_hotel_included, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bindItems(context!!, position, arrData[position], recyclerClickListener)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: HotelRoom,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model.RoomType != null) {
                itemView.radioName.text = model.RoomType
            }

            if (model.isSelected) {
                itemView.imgCheck.setImageResource(R.drawable.ic_radio_check)
            } else {
                itemView.imgCheck.setImageResource(R.drawable.ic_radio_uncheck)
            }

            itemView.llRow.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, adapterPosition, 1008)
            }
        }
    }

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


