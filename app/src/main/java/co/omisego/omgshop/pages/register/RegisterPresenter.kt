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
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.helpers.Validator
import co.omisego.omgshop.models.Register
import co.omisego.omgshop.network.OMGApiManager

class RegisterPresenter(private val sharePrefsManager: SharePrefsManager, private val validator: Validator = Validator()) : BasePresenterImpl<RegisterContract.View>(), RegisterContract.Presenter {
    override fun handleRegister(request: Register.Request) {
        mCompositeSubscription += OMGApiManager.register(request)
                .doOnSubscribe { mView?.showLoading() }
                .doFinally { mView?.hideLoading() }
                .subscribe({
                    sharePrefsManager.saveCredential(it.data)
                    mView?.showRegisterSuccess(it.data)
                }, {
                    mView?.showRegisterFailed(it.errorResponse().data)
                })
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