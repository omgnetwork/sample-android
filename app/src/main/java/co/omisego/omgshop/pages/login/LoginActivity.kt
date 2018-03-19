package co.omisego.omgshop.pages.login

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/24/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.custom.MinimalTextChangeListener
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.pages.products.ProductListActivity
import co.omisego.omgshop.pages.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.View, LoginContract.Presenter>(), LoginContract.View {
    override val mPresenter: LoginContract.Presenter by lazy { LoginPresenter(SharePrefsManager(this)) }
    private lateinit var mLoadingDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initInstance()
    }

    private fun initInstance() {
        mPresenter.checkHasLogin()

        mLoadingDialog = ProgressDialog(this)
        mLoadingDialog.setTitle(R.string.activity_login_loading_dialog_title)
        mLoadingDialog.setMessage(getString(R.string.activity_login_loading_dialog_message))

        btnLogin.setOnClickListener {
            val request = Login.Request(etEmail.text.toString(), etPassword.text.toString())
            if (mPresenter.validateEmail(request.email) && mPresenter.validatePassword(request.password)) {
                mPresenter.handleLogin(request)
            }
        }
        val clickSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                mPresenter.handleClickRegisterButton()
            }
        }

        val ss = SpannableString(getString(R.string.activity_login_register))

        // To make the word "Register" clickable.
        ss.setSpan(clickSpan, 27, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvRegister.text = ss
        tvRegister.movementMethod = LinkMovementMethod.getInstance()

        etEmail.addTextChangedListener(MinimalTextChangeListener {
            tilEmail.isErrorEnabled = !mPresenter.validateEmail(it.toString())
        })

        etPassword.addTextChangedListener(MinimalTextChangeListener {
            tilPassword.isErrorEnabled = !mPresenter.validatePassword(it.toString())
        })
    }

    override fun showLoginSuccess(response: Credential) {
        startActivity(Intent(this@LoginActivity, ProductListActivity::class.java))
        finish()
    }

    override fun showLoginFailed(response: Error) {
        showMessage(response.description)

    }

    override fun showEmailErrorHint(msg: String) {
        tilEmail.error = msg
        tilEmail.isErrorEnabled = true
    }

    override fun showPasswordErrorHint(msg: String) {
        tilPassword.error = msg
        tilPassword.isErrorEnabled = true
    }

    override fun showRegister() {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    override fun showLoading() {
        mLoadingDialog.show()
    }

    override fun hideLoading() {
        mLoadingDialog.dismiss()
    }
}
