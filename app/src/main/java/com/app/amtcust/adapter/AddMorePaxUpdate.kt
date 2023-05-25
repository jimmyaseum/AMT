package com.app.amtcust.adapter

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.TourPaxInformationModel
import kotlinx.android.synthetic.main.adapter_more_pax_info.view.*
import kotlin.collections.ArrayList

class AddMorePaxUpdate(val context: Context?, private val arrData: ArrayList<TourPaxInformationModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<AddMorePaxUpdate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_more_pax_info, parent, false)
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
        lateinit var adapterPAXInner: AddMorePaxInnerUpdate

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<TourPaxInformationModel>
        ) {
            this.context = context

            if(model[position].RoomNo != null) {
                itemView.txtRoomNo.text = "Room No : " +model[position].RoomNo
            }

            val arrayList = model[position].paxData!!

            if(arrayList.size > 0) {
                adapterPAXInner = AddMorePaxInnerUpdate(this.context, arrayList)
                itemView.rvPaxInfoInner.adapter = adapterPAXInner
            }
        }
    }
}
