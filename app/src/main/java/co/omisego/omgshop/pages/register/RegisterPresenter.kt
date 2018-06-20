package co.omisego.omgshop.pages.register

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.R
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.Contextor.context
import co.omisego.omgshop.helpers.Validator
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Response
import co.omisego.omgshop.pages.register.caller.RegisterCaller
import co.omisego.omgshop.pages.register.caller.RegisterCallerContract

class RegisterPresenter(
    private val validator: Validator = Validator()
) : BasePresenterImpl<RegisterContract.View, RegisterCallerContract.Caller>(), RegisterContract.Presenter, RegisterCallerContract.Handler {

    override var caller: RegisterCallerContract.Caller? = RegisterCaller(this)

    override fun handleRegisterFailed(error: Throwable) {
        mView?.hideLoading()
        mView?.showRegisterFailed(error.errorResponse().data)
    }

    override fun handleRegisterSuccess(response: Response<Credential>) {
        mView?.hideLoading()
        mView?.showRegisterSuccess(response.data)
    }

    override fun showLoading() {
        mView?.showLoading()
    }

    override fun validateEmail(email: String): Boolean {
        if (!validator.validateEmail(email)) {
            mView?.showEmailErrorHint(context.getString(R.string.activity_register_email_error_validation))
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

    override fun validateFirstName(firstName: String): Boolean {
        if (!validator.validateFirstName(firstName)) {
            mView?.showFirstNameErrorHint(context.getString(R.string.activity_register_first_name_error_validation))
            return false
        }
        return true
    }

    override fun validateLastName(lastName: String): Boolean {
        if (!validator.validateLastName(lastName)) {
            mView?.showLastNameErrorHint(context.getString(R.string.activity_register_last_name_error_validation))
            return false
        }
        return true
    }
}