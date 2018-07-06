package co.omisego.omgshop.pages.transaction.showqr.consumptions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

class ConsumptionRecyclerAdapter(
    private val consumptionList: MutableList<TransactionConsumption>,
    private val listener: ConsumptionViewHolder.OnConfirmationClickListener
) : RecyclerView.Adapter<ConsumptionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_incoming_transaction_consumption, parent, false)
        return ConsumptionViewHolder(itemView, listener)
    }

    fun add(newConsumption: TransactionConsumption) {
        val diffCallback = ConsumptionDiffCallback(
            consumptionList,
            if (consumptionList.size > 0) {
                listOf(newConsumption) + consumptionList
            } else {
                listOf(newConsumption)
            }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        consumptionList.add(0, newConsumption)
    }

    fun update(updatedConsumption: TransactionConsumption) {
        val oldConsumptionIndex = consumptionList.indexOfLast { it.id == updatedConsumption.id }
        if (oldConsumptionIndex == -1) return

        val diffCallback = ConsumptionDiffCallback(
            consumptionList,
            consumptionList.subList(0, oldConsumptionIndex) +
                updatedConsumption +
                consumptionList.subList(oldConsumptionIndex + 1, consumptionList.size)
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        consumptionList[oldConsumptionIndex] = updatedConsumption
    }

    override fun getItemCount(): Int = consumptionList.size

    override fun onBindViewHolder(holder: ConsumptionViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) onBindViewHolder(holder, position)
        else {
            val bundle = payloads[0] as Bundle
            val consumption = bundle.getParcelable<TransactionConsumption>("consumption")
            holder.updateStatus(consumption.status)
        }
    }

    override fun onBindViewHolder(holder: ConsumptionViewHolder, position: Int) {
        holder.bind(consumptionList[position])
    }
}