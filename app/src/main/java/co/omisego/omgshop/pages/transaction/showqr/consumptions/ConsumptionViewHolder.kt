package co.omisego.omgshop.pages.transaction.showqr.consumptions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.widget.RecyclerView
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.helpers.TransactionViewHolderDecorator
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionStatus
import co.omisego.omisego.model.transaction.request.TransactionRequestType
import kotlinx.android.synthetic.main.viewholder_incoming_transaction_consumption.view.*
import java.text.SimpleDateFormat
import java.util.Locale

class ConsumptionViewHolder(val view: View, private val listener: OnConfirmationClickListener) : RecyclerView.ViewHolder(view) {
    private val dateFormat by lazy { SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US) }
    private val decorator by lazy { TransactionViewHolderDecorator(itemView.context) }
    private lateinit var transactionConsumption: TransactionConsumption

    fun bind(consumption: TransactionConsumption) {
        transactionConsumption = consumption
        val sameAddress = consumption.transactionRequest.type == TransactionRequestType.SEND
        with(transactionConsumption) {
            with(decorator) {
                setTransactionDirection(sameAddress) {
                    itemView.tvTransactionDirection.text = it
                }
                itemView.tvTransactionAddress.text = address
                colorizedTransactionAmount(sameAddress) {
                    itemView.tvTransactionAmount.setTextColor(it)
                }
                status.colorizedTransactionStatus {
                    itemView.tvTransactionStatus.setTextColor(it)
                }
                formatTransactionAmount(sameAddress) {
                    itemView.tvTransactionAmount.text = it
                }
                itemView.tvTransactionStatus.text = context.getString(R.string.transaction_status, status.value.capitalize())
                itemView.tvTransactionDate.text = dateFormat.format(createdAt)
            }
            val confirmationVisibility = if (status == TransactionConsumptionStatus.PENDING) View.VISIBLE else View.GONE
            itemView.ivApprove.visibility = confirmationVisibility
            itemView.ivReject.visibility = confirmationVisibility
            itemView.ivApprove.setOnClickListener { listener.onClickApprove(transactionConsumption) }
            itemView.ivReject.setOnClickListener { listener.onClickReject(transactionConsumption) }
        }
    }

    fun updateStatus(newStatus: TransactionConsumptionStatus) {
        transactionConsumption = transactionConsumption.copy(status = newStatus)
        itemView.tvTransactionStatus.text = itemView.context.getString(
            R.string.transaction_status,
            newStatus.value.capitalize()
        )
        with(decorator) {
            newStatus.colorizedTransactionStatus {
                itemView.tvTransactionStatus.setTextColor(it)
            }
        }
        itemView.ivApprove.visibility = View.GONE
        itemView.ivReject.visibility = View.GONE
    }

    interface OnConfirmationClickListener {
        fun onClickApprove(transactionConsumption: TransactionConsumption)
        fun onClickReject(transactionConsumption: TransactionConsumption)
    }
}
