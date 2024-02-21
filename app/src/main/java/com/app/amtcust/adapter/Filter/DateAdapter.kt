package com.app.amtcust.adapter.Filter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.DateDataModel
import com.app.amtcust.utils.convertDateStringToString
import kotlinx.android.synthetic.main.row_date.view.*
import java.util.*

class DateAdapter(private val mContext: Context, private val arrData: ArrayList<DateDataModel>) : RecyclerView.Adapter<DateAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_date, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mContext,arrData[position])

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            mContext: Context,
            model: DateDataModel) {

            if(model.TourDate != null) {
                val mDay = convertDateStringToString(model.TourDate,"dd/MM/yyyy","EEE")
                itemView.txtDay.text = mDay

                val mDate = convertDateStringToString(model.TourDate,"dd/MM/yyyy","dd")
                itemView.txtDate.text = mDate

                val mYear = convertDateStringToString(model.TourDate,"dd/MM/yyyy","yyyy")
                itemView.txtYear.text = mYear

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

}