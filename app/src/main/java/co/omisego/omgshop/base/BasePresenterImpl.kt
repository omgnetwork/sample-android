package co.omisego.omgshop.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenterImpl<V : BaseContract.BaseView> : BaseContract.BasePresenter<V> {
    protected var mView: V? = null
    protected var mCompositeSubscription: CompositeDisposable? = null

    override fun attachView(view: V) {
        mCompositeSubscription = CompositeDisposable()
        mView = view
    }

    override fun detachView() {
        mView = null
        mCompositeSubscription?.dispose()
        mCompositeSubscription = null
    }

    override fun CompositeDisposable?.plusAssign(d: Disposable) {
        mCompositeSubscription?.add(d)
    }

    override fun unsubscription() {
        mCompositeSubscription?.clear()
    }

    fun log(message: String) {
        Log.d(this.javaClass.simpleName, message)
    }

}