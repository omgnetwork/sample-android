package co.omisego.omgshop.pages.history

import android.support.v7.util.DiffUtil
import co.omisego.omisego.model.transaction.Transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionHistoryDiffCallback(
    private val oldTransactionList: List<Transaction>,
    private val newTransactionList: List<Transaction>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTransactionList[oldItemPosition].id == newTransactionList[newItemPosition].id
    }

    override fun getOldListSize() = oldTransactionList.size

    override fun getNewListSize() = newTransactionList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }
}
