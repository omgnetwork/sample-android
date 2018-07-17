package co.omisego.omgshop.pages.profile

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class MyProfileBalanceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var state: MyBalanceListState = MyBalanceListState.Loading()
        set(value) {
            changeState(field, value)
            field = value
        }

    private fun changeState(oldState: MyBalanceListState, newState: MyBalanceListState) {
        if (oldState.viewType == newState.viewType) return
        val diffCallback = when (newState) {
            is MyBalanceListState.Loading -> MyBalanceDiffCallback(mutableListOf(), newState.listBalance)
            is MyBalanceListState.MyBalance -> MyBalanceDiffCallback(oldState.listBalance, newState.listBalance)
        }
        val diff = DiffUtil.calculateDiff(diffCallback)
        diff.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        state.onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = state.createViewHolder(parent, viewType)
    override fun getItemViewType(position: Int) = state.getItemViewType(position)
    override fun getItemCount() = state.itemCount
}
