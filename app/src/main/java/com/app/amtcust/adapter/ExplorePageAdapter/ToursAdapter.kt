package com.app.amtcust.adapter.ExplorePageAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.TourListModel
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_tours.view.LLPlaces
import kotlinx.android.synthetic.main.adapter_tours.view.img
import kotlinx.android.synthetic.main.adapter_tours.view.txtName

class ToursAdapter(val context: Context?, private val arrData: ArrayList<TourListModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<ToursAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_tours, parent, false)
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
            model: ArrayList<TourListModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].TourName != null) {
                itemView.txtName.text = model[position].TourName
            }
            if( model[position].TourImage != null) {
                itemView.img.loadUrlRoundedCorner2(
                    model[position].TourImage,
                    R.drawable.no_image,
                    5
                )
            }

            itemView.LLPlaces.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 103)
            }
        }
    }
}
