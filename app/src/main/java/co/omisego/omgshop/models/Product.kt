package co.omisego.omgshop.models

import com.google.gson.annotations.SerializedName


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object Product {
    object Get {
        data class Response(
                val data: List<Item>
        )

        data class Item(
                val id: String,
                val name: String,
                val description: String,
                @SerializedName("image_url") val imageUrl: String,
                val price: Int
        )
    }

    object Buy {
        data class Request(@SerializedName("token_id") val tokenId: String,
                           @SerializedName("token_value") val tokenValue: Int,
                           @SerializedName("product_id") val productId: String)

    }
}
