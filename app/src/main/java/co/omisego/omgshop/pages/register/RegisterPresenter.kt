package co.omisego.omgshop.pages.register

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.helpers.Validator
import co.omisego.omgshop.models.Register
import co.omisego.omgshop.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class RegisterPresenter(val sharePrefsManager: SharePrefsManager, val validator: Validator = Validator()) : BasePresenterImpl<RegisterContract.View>(), RegisterContract.Presenter {
    override fun handleRegister(request: Register.Request) {
        mCompositeSubscription += ApiClient.omiseGO.signup(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sharePrefsManager.saveRegisterResponse(it.data)
                    mView?.showRegisterSuccess(it.data)
                    log(it.toString())
                }, {
                    mView?.showMessage(it.errorResponse().data.description)
                    mView?.showRegisterFailed(it.errorResponse().data)
                })
    }

    override fun validateEmail(email: String): Boolean {
        if (!validator.validateEmail(email)) {
            mView?.showEmailErrorHint("Email address is not valid")
            return false
        }
        return true
    }

    override fun validatePassword(password: String): Boolean {
        if (!validator.validatePassword(password)) {
            mView?.showPasswordErrorHint("Password length should not less than 8 characters")
            return false
        }
        return true
    }

    override fun validateFirstName(firstName: String): Boolean {
        if (!validator.validateFirstName(firstName)) {
            mView?.showFirstNameErrorHint("First name must not be empty")
            return false
        }
        return true
    }

    override fun validateLastName(lastName: String): Boolean {
        if (!validator.validateLastName(lastName)) {
            mView?.showLastNameErrorHint("Last name must not be empty")
            return false
        }
        return true
    }
}