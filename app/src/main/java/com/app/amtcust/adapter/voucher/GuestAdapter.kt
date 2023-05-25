package com.app.amtcust.adapter.voucher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.GuestDetailsModel
import kotlinx.android.synthetic.main.adapter_guest_detail.view.*


class GuestAdapter(val context: Context?, private val arrData: ArrayList<GuestDetailsModel>) : RecyclerView.Adapter<GuestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_guest_detail, parent, false)
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
            model: ArrayList<GuestDetailsModel>
        ) {
            this.context = context

            if(model[position].Name != null && model[position].Name != "") {
                itemView.txtGuestName.text = model[position].Name
            }
            if(model[position].MobileNo != null && model[position].MobileNo != "") {
                itemView.txtGuestMobile.text = model[position].MobileNo
            }

        }
    }
}


