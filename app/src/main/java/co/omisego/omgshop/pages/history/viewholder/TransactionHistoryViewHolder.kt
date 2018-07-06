package co.omisego.omgshop.pages.history.viewholder

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.widget.RecyclerView
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.helpers.TransactionViewHolderDecorator
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import kotlinx.android.synthetic.main.viewholder_transaction_record.view.*
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionHistoryViewHolder(
    itemView: View,
    private val myAddress: String
) : RecyclerView.ViewHolder(itemView) {
    private val dateFormat by lazy { SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US) }
    private val decorator by lazy { TransactionViewHolderDecorator(itemView.context) }

    fun bindItem(record: Transaction) {
        with(record) {
            val isSameAddress = isSameAddress(record.from)
            with(decorator) {
                setTransactionDirection(isSameAddress) {
                    itemView.tvTransactionDirection.text = it
                }
                setTransactionAddress(isSameAddress) {
                    itemView.tvTransactionAddress.text = it
                }
                colorizedTransactionAmount(isSameAddress) {
                    itemView.tvTransactionAmount.setTextColor(it)
                }
                status.colorizedTransactionStatus {
                    itemView.tvTransactionStatus.setTextColor(it)
                }
                from.formatTransactionAmount(isSameAddress) {
                    itemView.tvTransactionAmount.text = it
                }
                itemView.tvTransactionStatus.text = context.getString(R.string.transaction_status, status.value.capitalize())
                itemView.tvTransactionDate.text = dateFormat.format(createdAt)
            }
        }
    }

    private fun isSameAddress(source: TransactionSource) = myAddress == source.address
}
