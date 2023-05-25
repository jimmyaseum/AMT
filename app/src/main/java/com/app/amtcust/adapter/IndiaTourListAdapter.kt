package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.SectorListModel
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_india_tour_list.view.*
import kotlinx.android.synthetic.main.adapter_india_tour_list.view.LLPlaces
import kotlinx.android.synthetic.main.adapter_india_tour_list.view.img

class IndiaTourListAdapter(val context: Context?, private val arrData: ArrayList<SectorListModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<IndiaTourListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_india_tour_list, parent, false)
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

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<SectorListModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].SectorName != null) {
                itemView.txtName.text = model[position].SectorName
                itemView.txtName1.text = model[position].SectorName
            }
            if( model[position].DestinationImage != null && model[position].DestinationImage != "") {
                itemView.img.loadUrlRoundedCorner2(
                    model[position].DestinationImage,
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
            itemView.LLPlaces.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 107)
            }
        }
    }
}
