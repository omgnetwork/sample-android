package co.omisego.omgshop.pages.profile

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 28/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.util.DiffUtil
import co.omisego.omisego.model.Balance

class MyBalanceDiffCallback(
    private val oldList: MutableList<Pair<Balance, Boolean>>,
    private val newList: MutableList<Pair<Balance, Boolean>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Skip header
        if (oldItemPosition == 0 || newItemPosition == 0) return true
        return oldList[oldItemPosition - 1].first.token.id == newList[newItemPosition - 1].first.token.id
    }

    override fun getOldListSize() = oldList.size + 1
    override fun getNewListSize() = newList.size + 1
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition == 0 || newItemPosition == 0) return true
        return oldList[oldItemPosition - 1].second == newList[newItemPosition - 1].second
    }
}
