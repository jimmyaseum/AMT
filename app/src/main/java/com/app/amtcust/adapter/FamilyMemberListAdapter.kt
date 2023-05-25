package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.FamilyMemberListModel
import com.app.amtcust.utils.loadUrlRoundedCorner2
import kotlinx.android.synthetic.main.adapter_family_member_list.view.*
import kotlinx.android.synthetic.main.adapter_family_member_list.view.txtName

class FamilyMemberListAdapter(val context: Context?, private val arrData: ArrayList<FamilyMemberListModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<FamilyMemberListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_family_member_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position,arrData, recyclerClickListener)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<FamilyMemberListModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].FirstName != null) {
                itemView.txtName.text = model[position].FirstName + " " + model[position].LastName
            }

            if(model[position].MobileNo != null) {
                itemView.txtMobile.text = model[position].MobileNo
            }

            if(model[position].EmailID != null) {
                itemView.txtEmail.text = model[position].EmailID
            }

            if( model[position].CustomerImage != null && model[position].CustomerImage != "") {
                itemView.profile_image.loadUrlRoundedCorner2(
                    model[position].CustomerImage,
                    R.drawable.no_image,
                    5
                )
            } else {
                itemView.profile_image.loadUrlRoundedCorner2(
                    "",
                    R.drawable.no_image_placeholder,
                    5
                )
            }
            itemView.ImgMore.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 110)
            }

        }
    }
}
