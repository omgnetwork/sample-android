package co.omisego.omgshop.pages.history.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omgshop.pages.history.ItemType
import co.omisego.omgshop.pages.history.TransactionHistoryDiffCallback
import co.omisego.omgshop.pages.history.viewholder.LoadMoreViewHolder
import co.omisego.omgshop.pages.history.viewholder.TransactionHistoryViewHolder
import co.omisego.omgshop.pages.history.viewholder.listener.LoadMoreCommand
import co.omisego.omgshop.pages.history.viewholder.listener.OnLoadListener
import co.omisego.omisego.model.transaction.list.Transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionHistoryAdapter(
    private val transactionList: MutableList<Transaction> = mutableListOf(),
    private val loadMoreCommand: LoadMoreCommand,
    var myAddress: String = ""
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var loadingListener: OnLoadListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_transaction_record, parent, false)
                TransactionHistoryViewHolder(view, myAddress)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_load_more, parent, false)
                val loadMoreViewHolder = LoadMoreViewHolder(view, loadMoreCommand)
                loadingListener = loadMoreViewHolder
                return loadMoreViewHolder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (transactionList.size == position || transactionList.isEmpty()) ItemType.TYPE_LOAD_MORE
        else ItemType.TYPE_ITEM
    }

    override fun getItemCount() = transactionList.size + 1 // One more row is for load more.

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TransactionHistoryViewHolder -> holder.bindItem(transactionList[position])
            is LoadMoreViewHolder -> holder.bindClick()
        }
    }

    fun getLoadingListener() = loadingListener

    fun addTransactions(newTransactionList: List<Transaction>) {
        val transactionHistoryDiffCallback = TransactionHistoryDiffCallback(
            this.transactionList,
            this.transactionList + newTransactionList
        )
        val diffResult = DiffUtil.calculateDiff(transactionHistoryDiffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.transactionList.addAll(newTransactionList)
    }

    fun clear() {
        val transactionHistoryDiffCallback = TransactionHistoryDiffCallback(
            this.transactionList,
            mutableListOf()
        )
        val diffResult = DiffUtil.calculateDiff(transactionHistoryDiffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.transactionList.clear()
    }
}