package co.omisego.omgshop.pages.history.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.pages.history.PaginationConfig
import co.omisego.omgshop.pages.history.viewholder.listener.LoadMoreCommand
import co.omisego.omgshop.pages.history.viewholder.listener.OnLoadListener
import kotlinx.android.synthetic.main.viewholder_load_more.view.*

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class LoadMoreViewHolder(
        itemView: View,
        private val command: LoadMoreCommand
) : RecyclerView.ViewHolder(itemView), OnLoadListener {
    fun bindClick() {
        if (itemView.isEnabled)
            itemView.tvLoadMore.text = itemView.context.getString(R.string.load_more, PaginationConfig.PER_PAGE)
        itemView.setOnClickListener {
            setViewLoading(true)
            command.onLoadMore()
        }
    }

    override fun onFinished() {
        setViewLoading(false)
    }

    override fun onReachedLastPage() {
        setViewEnabled(false)
    }

    override fun onReloaded() {
        setViewEnabled(true)
    }

    private fun setViewEnabled(enabled: Boolean) {
        if (enabled) {
            itemView.alpha = 1f
            itemView.isEnabled = true
            itemView.tvLoadMore.text = itemView.context.getString(R.string.load_more, PaginationConfig.PER_PAGE)
        } else {
            itemView.alpha = 0.5f
            itemView.isEnabled = false
            itemView.tvLoadMore.text = itemView.context.getString(R.string.load_more_disable)
        }
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