package com.app.amtcust.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.Facility
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_facility_included.view.*

class FacilityAdapter(val context: Context?, private val arrData: ArrayList<Facility>) : RecyclerView.Adapter<FacilityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_facility_included, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bindItems(context!!, arrData[position])
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            model: Facility
        ) {
            this.context = context

//            Log.e("model","========>"+model[position])

            if(model.Name != null) {
                itemView.txt_facility_name.text = model.Name
            }
            if( model.Image != null && model.Image != "") {
                itemView.img_facility.loadUrlRoundedCorner2(
                    model.Image,
                    R.drawable.ic_image,
                    5
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


