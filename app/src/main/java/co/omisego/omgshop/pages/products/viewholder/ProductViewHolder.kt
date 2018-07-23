package co.omisego.omgshop.pages.products.viewholder

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import co.omisego.omgshop.extensions.thousandSeparator
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.products.listener.ProductListenerHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.viewholder_product.view.*

class ProductViewHolder(
    itemView: View,
    productListenerDelegator: ProductListenerHolder
) : RecyclerView.ViewHolder(itemView), ProductListenerHolder by productListenerDelegator {
    private val tvTitle = itemView.tvTitle
    private val tvDescription = itemView.tvDescription
    private val ivLogo = itemView.ivLogo
    private val btnPrice = itemView.btnPrice

    @SuppressLint("SetTextI18n")
    fun bind(model: Product.Get.Item) {
        with(model) {
            tvTitle.text = name
            tvDescription.text = description
            Glide.with(itemView)
                .load(imageUrl)
                .apply(RequestOptions().transforms(RoundedCorners(20)))
                .into(ivLogo)
            btnPrice.text = "฿${price.toDouble().thousandSeparator()}"
            btnPrice.setOnClickListener { productListener?.onProductClick(id) }
        }
    }
}
