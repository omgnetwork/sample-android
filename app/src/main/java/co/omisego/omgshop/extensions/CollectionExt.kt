package co.omisego.omgshop.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
operator fun <T> MutableList<T>.plus(another: MutableList<T>): MutableList<T> {
    val newList = mutableListOf<T>()
    newList.addAll(this)
    newList.addAll(another)
    return newList
}