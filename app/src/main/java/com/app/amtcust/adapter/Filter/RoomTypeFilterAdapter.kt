package com.app.amtcust.adapter.Filter

import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.RoomTypeModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_category_filter.view.*

class RoomTypeFilterAdapter(
    val arrayList: ArrayList<RoomTypeModel>?,
    val recyclerClickListener: RecyclerClickListener
) : RecyclerView.Adapter<RoomTypeFilterAdapter.ViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_category_filter, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, arrayList!![position], recyclerClickListener)
    }

    override fun getItemCount(): Int {
        if (arrayList.isNullOrEmpty()) {
            return 0
        }
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            context: Context,
            model: RoomTypeModel,
            recyclerClickListener: RecyclerClickListener
        ) {
            itemView.tvName.text = model.Title

            if (model.isSelected) {
                itemView.imgCheck.isChecked = true

            } else {
                itemView.imgCheck.isChecked = false
            }

            itemView.llContent.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, adapterPosition, 1002)
            }
        }
    }

    fun updateItem(position: Int) {

        if (!arrayList.isNullOrEmpty()) {
            arrayList[position].isSelected = !arrayList[position].isSelected
        }
        notifyDataSetChanged()
    }
    // Single Filter Selection
    fun updateItemSingle(position: Int) {

        for (i in arrayList?.indices!!) {
            if (arrayList[i].isSelected) {
                arrayList[i].isSelected = false
            }
        }
        arrayList[position].isSelected = true
        notifyDataSetChanged()
    }

    fun clearSelectedItems() {

        if (!arrayList.isNullOrEmpty()) {
            for (model in arrayList) {
                model.isSelected = false
            }
            notifyDataSetChanged()
        }
    }
}