package co.omisego.omgshop.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import co.omisego.omgshop.base.BaseContract.BaseCaller
import co.omisego.omgshop.base.BaseContract.BasePresenter
import co.omisego.omgshop.base.BaseContract.BaseView
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.pages.login.LoginActivity

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<in V : BaseView, C : BaseCaller, out P : BasePresenter<V, C>> : AppCompatActivity(), BaseView {
    protected abstract val mPresenter: P
    private var mLoadingView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
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

    fun log(message: String) {
        Log.d(this.javaClass.simpleName, message)
    }

    override fun clearTokenAndGotoLogin() {
        Preference.saveCredential(Credential("", "", ""))
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}