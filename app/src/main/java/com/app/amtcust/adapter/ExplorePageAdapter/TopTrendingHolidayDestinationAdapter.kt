package com.app.amtcust.adapter.ExplorePageAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.TopTrendListModel
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_trending_destinations.view.LLPlaces
import kotlinx.android.synthetic.main.adapter_trending_destinations.view.img
import kotlinx.android.synthetic.main.adapter_trending_destinations.view.txtName

class TopTrendingHolidayDestinationAdapter(val context: Context?, private val arrData: ArrayList<TopTrendListModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<TopTrendingHolidayDestinationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_trending_destinations, parent, false)
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
            model: ArrayList<TopTrendListModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].SectorName != null) {
                itemView.txtName.text = model[position].SectorName
            }
            if( model[position].DestinationImage != null) {
                itemView.img.loadUrlRoundedCorner2(
                    model[position].DestinationImage,
                    R.drawable.no_image,
                    5
                )
            }
            itemView.LLPlaces.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 102)
            }
        }
    }
}