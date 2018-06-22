package co.omisego.omgshop.pages.login

import co.omisego.omgshop.R
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.Contextor.context
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.helpers.Validator
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Response
import co.omisego.omgshop.pages.login.caller.LoginCaller
import co.omisego.omgshop.pages.login.caller.LoginCallerContract

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LoginPresenter(
    private val validator: Validator = Validator()
) : BasePresenterImpl<LoginContract.View, LoginCallerContract.Caller>(), LoginContract.Presenter, LoginCallerContract.Handler {
    override var caller: LoginCallerContract.Caller? = LoginCaller(this)

    override fun handleLoginSuccess(response: Response<Credential>) {
        mView?.hideLoading()
        Preference.saveCredential(response.data)
        mView?.showLoginSuccess(response.data)
    }

    override fun handleLoginFailed(error: Throwable) {
        mView?.hideLoading()
        error.printStackTrace()
        mView?.showMessage(error.errorResponse().data.description)
        mView?.showLoginFailed(error.errorResponse().data)
    }

    override fun handleClickRegisterButton() {
        mView?.showRegister()
    }

    override fun showLoading() {
        mView?.showLoading()
    }

    override fun checkHasLogin() {
        val response = Preference.loadCredential()
        if (response.userId.isNotEmpty()) {
            mView?.showLoginSuccess(response)
        }
    }

    override fun validateEmail(email: String): Boolean {
        if (!validator.validateEmail(email)) {
            mView?.showEmailErrorHint(context.getString(R.string.activity_login_email_error_hint))
            return false
        }
        return true
    }

    override fun validatePassword(password: String): Boolean {
        if (!validator.validatePassword(password)) {
            mView?.showPasswordErrorHint(context.getString(R.string.activity_login_password_error_hint))
            return false
        }
        return true
    }
}