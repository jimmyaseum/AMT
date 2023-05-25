package com.app.amtcust.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.model.response.DocumentListModel
import com.app.amtcust.utils.visible
import kotlinx.android.synthetic.main.adapter_my_documents.view.*

class MyDocumentAdapter(val context: Context?, private val arrData: ArrayList<DocumentListModel>) : RecyclerView.Adapter<MyDocumentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_my_documents, parent, false)
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
            model: ArrayList<DocumentListModel>
        ) {
            this.context = context

            if(model[position].DocumentType != null) {
                itemView.txtDocType.text = model[position].DocumentType
            }

            if(model[position].DocumentNo != null) {
                itemView.txtDocNum.text = model[position].DocumentNo
            }

            if(model[position].DocumentCopy != null && model[position].DocumentCopy != "") {
                itemView.LLcardButtonView.visible()
                itemView.LLcardButtonView.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model[position].DocumentCopy))
                    context.startActivity(browserIntent)
                }
            }
        }
    }
}

