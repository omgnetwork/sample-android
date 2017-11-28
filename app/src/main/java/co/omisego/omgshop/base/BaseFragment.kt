package co.omisego.omgshop.base

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

abstract class BaseFragment<in V : BaseContract.BaseView, out P : BaseContract.BasePresenter<V>> : Fragment(), BaseContract.BaseView {
    protected abstract val mPresenter: P

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter.attachView(this as V)
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.detachView()
    }

    override fun View.hideLoading() {
        visibility = View.GONE
    }

    fun log(message: String) {
        Log.d(this.javaClass.name, message)
    }

}