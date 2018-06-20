package co.omisego.omgshop.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast

@Suppress("UNCHECKED_CAST")
abstract class BaseDialogFragment<in V : BaseContract.BaseView, C : BaseContract.BaseCaller, out P : BaseContract.BasePresenter<V, C>> : DialogFragment(), BaseContract.BaseView {
    protected abstract val mPresenter: P
    private lateinit var mLoadingView: View

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mPresenter.attachView(this as V)
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.detachView()
    }

    fun log(message: String) {
        Log.d(this.javaClass.name, message)
    }

    override fun onResume() {
        super.onResume()
        val size = Point()
        val window = dialog.window
        val display = window.windowManager.defaultDisplay
        display.getSize(size)
        window.setLayout((size.x * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun showLoading() {
        mLoadingView.visibility = View.VISIBLE
    }

    override fun clearTokenAndGotoLogin() {}

    override fun hideLoading() {
        mLoadingView.visibility = View.GONE
    }

    override fun setViewLoading(view: View) {
        mLoadingView = view
    }

    override fun showMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}