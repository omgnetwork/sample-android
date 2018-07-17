package co.omisego.omgshop.pages.profile

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omgshop.pages.profile.delegator.BalanceListenerDelegator
import co.omisego.omgshop.pages.profile.viewholder.BalanceListenerHolder
import co.omisego.omgshop.pages.profile.viewholder.MyProfileContentViewHolder
import co.omisego.omgshop.pages.profile.viewholder.UpdateBalanceAdapterListener
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import java.util.Date
import java.util.UUID

fun createEmptyBalance(): Balance {
    return Balance(0.bd, Token(UUID.randomUUID().toString(), "", "", 0.bd, Date(), Date(), mapOf(), mapOf()))
}

sealed class MyBalanceListState : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class MyBalance(
        override var listBalance: MutableList<Pair<Balance, Boolean>>,
        val adapter: MyProfileBalanceAdapter,
        delegator: BalanceListenerHolder = BalanceListenerDelegator()
    ) : MyBalanceListState(), BalanceListenerHolder by delegator, UpdateBalanceAdapterListener {
        override val viewType: Int = 2

        override fun onSelectedBalance(index: Int) {
            val newList = listBalance.mapIndexed { i, balancePair ->
                balancePair.first to (i == index)
            }
            val diffCallback = MyBalanceDiffCallback(listBalance, newList.toMutableList())
            val diff = DiffUtil.calculateDiff(diffCallback)
            diff.dispatchUpdatesTo(adapter)
            listBalance.clear()
            listBalance.addAll(newList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == this.viewType) {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_content_my_profile, parent, false)
                MyProfileContentViewHolder(itemView, this, this)
            } else {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_header_my_profile, parent, false)
                object : RecyclerView.ViewHolder(itemView) {}
            }
        }

        override fun getItemCount() = listBalance.size + 1
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MyProfileContentViewHolder) {
                holder.bind(listBalance[position - 1])
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) 1 else viewType
        }
    }

    class Loading : MyBalanceListState() {
        override val viewType: Int = 3
        override var listBalance: MutableList<Pair<Balance, Boolean>> = mutableListOf(
            createEmptyBalance() to false,
            createEmptyBalance() to false,
            createEmptyBalance() to false
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = if (viewType == this.viewType) {
                LayoutInflater.from(parent.context).inflate(R.layout.viewholder_my_balance_loading, parent, false)
            } else {
                LayoutInflater.from(parent.context).inflate(R.layout.viewholder_header_my_profile, parent, false)
            }
            return object : RecyclerView.ViewHolder(itemView) {}
        }

        override fun getItemCount() = listBalance.size + 1
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
        override fun getItemViewType(position: Int): Int {
            return if (position == 0) 1 else viewType
        }
    }

    abstract val viewType: Int
    abstract var listBalance: MutableList<Pair<Balance, Boolean>>
}
