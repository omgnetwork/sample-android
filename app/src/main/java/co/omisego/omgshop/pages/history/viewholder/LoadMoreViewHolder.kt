package co.omisego.omgshop.pages.history.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.pages.history.PaginationConfig
import co.omisego.omgshop.pages.history.TransactionHistoryActivity
import kotlinx.android.synthetic.main.viewholder_load_more.view.*


/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class LoadMoreViewHolder(itemView: View, private val command: LoadMoreViewCommand) : RecyclerView.ViewHolder(itemView), TransactionHistoryActivity.ItemLoadingListener {
    fun bindClick() {
        itemView.tvLoadMore.text = itemView.context.getString(R.string.load_more, PaginationConfig.PER_PAGE)
        itemView.setOnClickListener {
            setViewLoading(true)
            command.loadMore()
        }
    }

    override fun onFinished() {
        setViewLoading(false)
    }

    private fun setViewLoading(loading: Boolean) {
        if (loading) {
            itemView.tvLoadMore.visibility = View.INVISIBLE
            itemView.progressBar.visibility = View.VISIBLE
            itemView.isEnabled = false
        } else {
            itemView.isEnabled = true
            itemView.tvLoadMore.visibility = View.VISIBLE
            itemView.progressBar.visibility = View.INVISIBLE
        }
    }
}