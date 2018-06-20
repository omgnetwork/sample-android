package co.omisego.omgshop.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
abstract class BaseCaller : BaseContract.BaseCaller {
    var mCompositeSubscription: CompositeDisposable? = null

    override fun createCompositeSubscription() {
        mCompositeSubscription = CompositeDisposable()
    }

    override fun disposeCompositeSubscription() {
        mCompositeSubscription?.dispose()
        mCompositeSubscription = null
    }

    override fun unsubscription() {
        mCompositeSubscription?.clear()
    }

    override fun CompositeDisposable?.plusAssign(d: Disposable) {
        mCompositeSubscription?.add(d)
    }
}
