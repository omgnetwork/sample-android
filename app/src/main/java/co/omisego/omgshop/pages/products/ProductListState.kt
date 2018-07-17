package co.omisego.omgshop.pages.products

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.products.listener.ProductListenerHolder
import co.omisego.omgshop.pages.products.viewholder.ProductViewHolder

sealed class ProductListState : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class Loading : ProductListState() {
        override val viewType: Int = 1
        override val productList: List<Product.Get.Item> by lazy {
            listOf(
                Product.Get.Item.createEmpty(),
                Product.Get.Item.createEmpty(),
                Product.Get.Item.createEmpty()
            )
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product_loading, parent, false)
            return object : RecyclerView.ViewHolder(itemView) {}
        }
        override fun getItemCount() = productList.size
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    }

    class Success(
        override val productList: List<Product.Get.Item>,
        productListenerDelegator: ProductListenerHolder
    ) : ProductListState(), ProductListenerHolder by productListenerDelegator {
        override val viewType: Int = 2
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product, parent, false)
            return ProductViewHolder(itemView, this)
        }
        override fun getItemCount() = productList.size
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ProductViewHolder).bind(productList[position])
        }
    }

    abstract val productList: List<Product.Get.Item>
    abstract val viewType: Int
}
