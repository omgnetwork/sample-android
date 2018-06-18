package co.omisego.omgshop.pages.login

import co.omisego.omgshop.R
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.Contextor.context
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.helpers.Validator
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.network.CombinedAPIManager

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LoginPresenter(
    private val validator: Validator = Validator()
) : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {
    override fun handleLogin(request: Login.Request) {
        mCompositeSubscription += CombinedAPIManager.login(request)
            .doOnSubscribe { mView?.showLoading() }
            .doFinally { mView?.hideLoading() }
            .subscribe({
                Preference.saveCredential(it.data)
                mView?.showLoginSuccess(it.data)
            }, {
                it.printStackTrace()
                mView?.showMessage(it.errorResponse().data.description)
                mView?.showLoginFailed(it.errorResponse().data)
            })
    }

    override fun handleClickRegisterButton() {
        mView?.showRegister()
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