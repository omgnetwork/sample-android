package co.omisego.omgshop.pages.products

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import co.omisego.omgshop.pages.products.delegator.ProductListenerDelegator
import co.omisego.omgshop.pages.products.listener.ProductListenerHolder

class ProductListRecyclerAdapter(
    private val productListenerDelegator: ProductListenerHolder = ProductListenerDelegator()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ProductListenerHolder by productListenerDelegator {
    var state: ProductListState = ProductListState.Loading()
        set(value) {
            field = dispatchDiffUpdates(field, value)
        }

    private fun dispatchDiffUpdates(oldState: ProductListState, newState: ProductListState): ProductListState {
        if (newState.viewType == oldState.viewType) return newState
        val diffCallback = when (newState) {
            is ProductListState.Loading -> {
                ProductListDiffCallback(
                    listOf(),
                    newState.productList
                )
            }
            is ProductListState.Success -> {
                ProductListDiffCallback(
                    oldState.productList,
                    newState.productList
                )
            }
        }
        val diff = DiffUtil.calculateDiff(diffCallback)
        diff.dispatchUpdatesTo(this)
        return newState
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = state.onCreateViewHolder(parent, viewType)
    override fun getItemViewType(position: Int): Int = state.viewType
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = state.onBindViewHolder(holder, position)
    override fun getItemCount() = state.itemCount
}
