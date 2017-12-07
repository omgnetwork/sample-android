package co.omisego.omgshop.base

import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface BaseContract {
    interface BaseView {
        fun showLoading()
        fun hideLoading()
        fun setViewLoading(view: View)
        fun showMessage(msg: String)
    }

    interface BasePresenter<in V : BaseView> {
        fun attachView(view: V)
        fun detachView()
        fun unsubscription()
        operator fun CompositeDisposable?.plusAssign(d: Disposable)
    }
}