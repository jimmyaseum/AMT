package com.app.amtcust.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.PaymentReceiptListModel
import kotlinx.android.synthetic.main.adapter_payment_receipt_list.view.*

class PaymentReceiptListAdapter(val context: Context?, private val arrData: ArrayList<PaymentReceiptListModel>, val recyclerClickListener: RecyclerClickListener) : RecyclerView.Adapter<PaymentReceiptListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_payment_receipt_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context!!, position, arrData, recyclerClickListener)
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var context: Context? = null

        fun bindItems(
            context: Context,
            position: Int,
            model: ArrayList<PaymentReceiptListModel>,
            recyclerClickListener: RecyclerClickListener
        ) {
            this.context = context

            if(model[position].ReceiptNo != null) {
                itemView.txtReceiptNo.text = model[position].ReceiptNo
            }
            if(model[position].ReceiptStatus != null) {
                itemView.txtStatus.text = model[position].ReceiptStatus
            }
            if(model[position].PaymentDate != null) {
                itemView.txtReceiptDate.text = model[position].PaymentDate
            }
            if(model[position].Amount != null) {
                itemView.txtAmount.text = model[position].Amount.toString()
            }
            if(model[position].BookingNo != null) {
                itemView.txtTourBokingNo.text = model[position].BookingNo
            }
            if(model[position].PaymentFor != null) {
                itemView.txtPaymentFor.text = model[position].PaymentFor
            }

            itemView.llStartView.setOnClickListener {
                recyclerClickListener.onItemClickEvent(it, position, 112)
            }
        }
    }
}
