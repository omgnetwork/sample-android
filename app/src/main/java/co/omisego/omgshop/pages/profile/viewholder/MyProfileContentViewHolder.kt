package co.omisego.omgshop.pages.profile.viewholder

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omisego.model.Balance
import kotlinx.android.synthetic.main.viewholder_content_my_profile.view.*

interface BalanceListener {
    fun save(balance: Balance)
}

interface BalanceListenerHolder {
    var balanceListener: BalanceListener?
}

interface UpdateBalanceAdapterListener {
    fun onSelectedBalance(index: Int)
}

class MyProfileContentViewHolder(
    itemView: View,
    delegator: BalanceListenerHolder,
    private var adapterListener: UpdateBalanceAdapterListener
) : RecyclerView.ViewHolder(itemView), BalanceListenerHolder by delegator {
    private val tvAmount = itemView.tvAmount
    private val tvToken = itemView.tvToken
    private val ivSelected = itemView.ivSelected
    private val layoutContainer = itemView.layoutContainer

    @SuppressLint("SetTextI18n")
    fun bind(balancePair: Pair<Balance, Boolean>) {
        val (balance, showSelected) = balancePair
        tvAmount.text = balance.displayAmount(0)
        tvToken.text = balance.token.symbol
        showSelectIconIfNeeded(showSelected)
        layoutContainer.setOnClickListener {
            balanceListener?.save(balance)
            adapterListener.onSelectedBalance(adapterPosition - 1)
        }
    }

    private fun showSelectIconIfNeeded(show: Boolean) {
        val drawable = if (show) {
            ContextCompat.getDrawable(itemView.context, R.drawable.ic_check_24dp)
        } else {
            null
        }
        ivSelected.setImageDrawable(drawable)
    }
}
