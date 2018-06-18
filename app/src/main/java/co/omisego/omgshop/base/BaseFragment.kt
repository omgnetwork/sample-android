@file:Suppress("UNCHECKED_CAST")

package co.omisego.omgshop.base

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.Toast

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

abstract class BaseFragment<in V : BaseContract.BaseView, out P : BaseContract.BasePresenter<V>> : Fragment(), BaseContract.BaseView {
    protected abstract val mPresenter: P
    private var mLoadingView: View? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter.attachView(this as V)
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.detachView()
    }

    override fun showLoading() {
        mLoadingView?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mLoadingView?.visibility = View.GONE
    }

    override fun setViewLoading(view: View) {
        mLoadingView = view
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun clearTokenAndGotoLogin() {}

    fun log(message: String) {
        Log.d(this.javaClass.name, message)
    }
}