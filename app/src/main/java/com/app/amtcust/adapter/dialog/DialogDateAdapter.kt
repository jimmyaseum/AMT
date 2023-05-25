package com.app.amtcust.adapter.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.TourDates
import kotlinx.android.synthetic.main.adapter_room_type_list.view.*

class DialogDateAdapter(val context: Context?, private val arrData: ArrayList<TourDates>) : RecyclerView.Adapter<DialogDateAdapter.ViewHolder>() {

    private var recyclerRowClick: RecyclerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_room_type_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bindItems(context!!, position, arrData, recyclerRowClick!!)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<TourDates>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].TourDate != null) {
                itemView.radioRoomType.text = model[position].TourDate
            }

            if (model[position].isSelected) {
                itemView.radioRoomType.isChecked = true
            } else {
                itemView.radioRoomType.isChecked = false
            }

            itemView.radioRoomType.setOnClickListener { view ->
                recyclerClickListener!!.onItemClickEvent(view, position, 1)
            }

        }
    }

    fun setRecyclerRowClick(recyclerRowClick: RecyclerClickListener) {
        this.recyclerRowClick = recyclerRowClick
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


