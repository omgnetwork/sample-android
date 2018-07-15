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
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.products.delegator.ProductListenerDelegator
import co.omisego.omgshop.pages.products.listener.HandleProductListener
import co.omisego.omgshop.pages.products.viewholder.ProductViewHolder

class ProductListRecyclerAdapter(
    private var productList: List<Product.Get.Item>,
    private val productListenerDelegator: HandleProductListener = ProductListenerDelegator()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), HandleProductListener by productListenerDelegator {
    var state: ProductListState = ProductListState.Loading()
        set(value) {
            val diffCallback = when (value) {
                is ProductListState.Loading -> {
                    ProductListDiffCallback(
                        listOf(),
                        value.productList
                    )
                }
                is ProductListState.Success -> {
                    ProductListDiffCallback(
                        field.productList,
                        value.productList
                    )
                }
            }
            val diff = DiffUtil.calculateDiff(diffCallback)
            diff.dispatchUpdatesTo(this)
            this.productList = value.productList
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = state.createViewHolder(parent)
    override fun getItemViewType(position: Int): Int = state.viewType

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            holder.bind(productList[position])
        }
    }

    override fun getItemCount(): Int {
        val currentState = state
        return if (currentState is ProductListState.Loading) {
            currentState.totalLoadingPlaceHolderItem
        } else {
            productList.size
        }
    }
}
