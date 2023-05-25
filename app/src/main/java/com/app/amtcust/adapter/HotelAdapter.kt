package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.HotelData
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_hotel_data.view.*


class HotelAdapter(val context: Context?, private val arrData: ArrayList<HotelData>) : RecyclerView.Adapter<HotelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_hotel_data, parent, false)
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
            model: ArrayList<HotelData>
        ) {
            this.context = context

            if(model[position].Days != 0) {
                itemView.txt_HotelDay.text = "Days " + model[position].Days
            }
            if(model[position].HotelName != null) {
                itemView.txt_HotelName.text = model[position].HotelName
            }
            if(model[position].HotelAddress != null) {
                itemView.txt_HotelAddress.text = model[position].HotelAddress
            }
            if(model[position].StarRating != null) {
                itemView.ratingBar.rating = model[position].StarRating!!.toFloat()
            }
            if(model[position].HotelImage != null && model[position].HotelImage != "") {
                itemView.txt_HotelImage.loadUrlRoundedCorner2(
                    model[position].HotelImage,
                    R.drawable.ic_image,
                    1
                )
            } else {
                itemView.txt_HotelImage.loadUrlRoundedCorner2(
                    "",
                    R.drawable.no_image_placeholder,
                    1
                )
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


