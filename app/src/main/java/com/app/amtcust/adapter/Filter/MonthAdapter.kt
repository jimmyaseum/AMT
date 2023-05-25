package com.app.amtcust.adapter.Filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.MonthDataModel
import kotlinx.android.synthetic.main.row_month.view.*
import java.util.*

class MonthAdapter(private val mContext: Context, private val arrData: ArrayList<MonthDataModel>,private val recyclerRowClick:  RecyclerClickListener) : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_month, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mContext,arrData[position], recyclerRowClick!!)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            mContext: Context,
            model: MonthDataModel,
            recyclerClickListener: RecyclerClickListener
        ) {

            if(model.isSelected) {
                itemView.txtName.setTextColor(mContext.resources.getColor(R.color.colorWhite))
                itemView.card.setCardBackgroundColor(mContext.resources.getColor(R.color.colorAccent))
            } else {
                itemView.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                itemView.card.setCardBackgroundColor(mContext.resources.getColor(R.color.colorWhite))
            }

            itemView.txtName.text = model.MonthName
            itemView.card.setOnClickListener { view ->
                recyclerClickListener!!.onItemClickEvent(view, position, 1)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
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