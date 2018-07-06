package co.omisego.omgshop.pages.transaction.showqr.consumptions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.support.v7.util.DiffUtil
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

class ConsumptionDiffCallback(
    private val oldConsumptionList: List<TransactionConsumption>,
    private val newConsumptionList: List<TransactionConsumption>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldConsumptionList[oldItemPosition].id == newConsumptionList[newItemPosition].id
    }

    override fun getOldListSize() = oldConsumptionList.size

    override fun getNewListSize() = newConsumptionList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldConsumptionList[oldItemPosition].status == newConsumptionList[newItemPosition].status
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        /* areContentsTheSame is return false, so both of transaction consumption status are different*/
        return Bundle().apply {
            putParcelable("consumption", newConsumptionList[newItemPosition])
        }
    }
}
