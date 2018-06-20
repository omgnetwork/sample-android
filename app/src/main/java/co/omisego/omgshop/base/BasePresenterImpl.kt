package co.omisego.omgshop.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.util.Log
import co.omisego.omgshop.extensions.isAuthError
import co.omisego.omgshop.models.Error
import co.omisego.omisego.model.APIError

abstract class BasePresenterImpl<V : BaseContract.BaseView, C : BaseContract.BaseCaller> : BaseContract.BasePresenter<V, C> {
    protected var mView: V? = null

    override fun attachView(view: V) {
        mView = view
        caller?.createCompositeSubscription()
    }

    override fun detachView() {
        mView = null
        caller?.disposeCompositeSubscription()
    }

    override fun goBackToLoginIfNeeded(error: APIError) {
        if (error.isAuthError())
            mView?.clearTokenAndGotoLogin()
    }

    override fun goBackToLoginIfNeeded(error: Error) {
        if (error.isAuthError())
            mView?.clearTokenAndGotoLogin()
    }

    fun log(message: String) {
        Log.d(this.javaClass.simpleName, message)
    }
}
