package com.app.amtcust.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.amtcust.R
import com.app.amtcust.interFase.RecyclerClickListener
import com.app.amtcust.model.response.PaymentReceiptListModel
import com.app.amtcust.utils.AppConstant
import com.app.amtcust.utils.gone
import com.app.amtcust.utils.isOnline
import com.app.amtcust.utils.toast
import com.app.amtcust.utils.visible
import kotlinx.android.synthetic.main.adapter_payment_receipt_list.view.*
import java.util.Locale

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

            if(model[position].IsCreatedFromApp != null && model[position].IsCreatedFromApp!!) {

                itemView.cardEdit.visible()

                itemView.cardEdit.setOnClickListener {
                    recyclerClickListener.onItemClickEvent(it, adapterPosition, 102)
                }

                if(model[position].ReceiptImage != null && model[position].ReceiptImage != "") {
                    itemView.cardAttachment.setOnClickListener {
                        if(isOnline(context)) {
                            if(model[position].ReceiptImage!!.contains(".pdf")) {
                                var format = "https://docs.google.com/gview?embedded=true&url=%s"
                                val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, model[position].ReceiptImage)
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
                                context.startActivity(browserIntent)
                            } else {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model[position].ReceiptImage))
                                context.startActivity(browserIntent)
                            }
                        } else {
                            context.toast(context.resources.getString(R.string.msg_no_internet), AppConstant.TOAST_SHORT)
                        }
                    }
                } else {
                    itemView.cardAttachment.gone()
                }

            }
        }
    }
}
