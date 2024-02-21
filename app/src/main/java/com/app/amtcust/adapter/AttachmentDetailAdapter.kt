package com.app.amtcust.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.AttachmentModel
import kotlinx.android.synthetic.main.adapter_attachments.view.*
import java.util.Locale

class AttachmentDetailAdapter(val mContext: Context,
                              val arrayList: ArrayList<AttachmentModel>?
) : RecyclerView.Adapter<AttachmentDetailAdapter.ViewHolder>() {

    var selectedItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_attachments, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(mContext, arrayList!![position])
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            mContext: Context,
            model: AttachmentModel
        ) {

            itemView.tvAttachmentName.setText(model.Title)

            itemView.txtViewDocument.setOnClickListener {

                if (model.Documents != "") {

                    if(model.Documents!!.contains(".pdf")) {
                        var format = "https://docs.google.com/gview?embedded=true&url=%s"
                        val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, model.Documents)
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                        mContext.startActivity(browserIntent)
                    } else {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.Documents))
                        mContext.startActivity(browserIntent)
                    }

                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.Documents))
                    mContext.startActivity(browserIntent)

                }
            }
        }
    }

    fun removeItem(position: Int) {
        if (!arrayList.isNullOrEmpty()) {
            arrayList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getSelectedPosition(): Int {
        return selectedItemPosition
    }
}