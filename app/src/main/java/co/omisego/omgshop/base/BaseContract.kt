package co.omisego.omgshop.base

import android.view.View
import co.omisego.omgshop.models.Error
import co.omisego.omisego.model.APIError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface BaseContract {
    interface BaseView {
        fun showLoading()
        fun hideLoading()
        fun setViewLoading(view: View)
        fun showMessage(msg: String)
        fun clearTokenAndGotoLogin()
    }

    interface BasePresenter<in V : BaseView, C : BaseCaller> {
        var caller: C?
        fun attachView(view: V)
        fun detachView()
        fun goBackToLoginIfNeeded(error: APIError)
        fun goBackToLoginIfNeeded(error: Error)
    }

    interface BaseCaller {
        fun createCompositeSubscription()
        fun disposeCompositeSubscription()
        fun unsubscription()
        operator fun CompositeDisposable?.plusAssign(d: Disposable)
    }
}