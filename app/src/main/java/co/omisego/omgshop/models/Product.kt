package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.UUID

@SuppressLint("ParcelCreator")
object Product {
    object Get {
        data class Response(
            val data: List<Item>
        )

        @Parcelize
        data class Item(
            val id: String,
            val name: String,
            val description: String,
            val imageUrl: String,
            val price: Int
        ) : Parcelable {
            companion object {
                fun createEmpty(): Item {
                    return Item(
                        UUID.randomUUID().toString(),
                        "",
                        "",
                        "",
                        0
                    )
                }
            }
        }
    }

    object Buy {
        data class Request(
            val tokenId: String,
            val tokenValue: BigDecimal,
            val productId: String
        )
    }
}
