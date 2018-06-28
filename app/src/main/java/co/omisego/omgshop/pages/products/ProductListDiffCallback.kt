package co.omisego.omgshop.pages.products

import android.support.v7.util.DiffUtil
import co.omisego.omgshop.models.Product

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 28/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ProductListDiffCallback(
    private val oldList: List<Product.Get.Item>,
    private val newList: List<Product.Get.Item>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        areItemsTheSame(oldItemPosition, newItemPosition)
}
