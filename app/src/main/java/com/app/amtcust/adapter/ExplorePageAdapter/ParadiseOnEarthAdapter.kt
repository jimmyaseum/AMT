package com.app.amtcust.adapter.ExplorePageAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import kotlinx.android.synthetic.main.adapter_paradise_earth.view.*

class ParadiseOnEarthAdapter(val context: Context?, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<ParadiseOnEarthAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_paradise_earth, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, recyclerClickListener)
    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context
            itemView.LLPlaces.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 106)
            }
        }
    }
}

