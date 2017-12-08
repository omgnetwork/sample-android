package co.omisego.omgshop.pages.login

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.helpers.Validator
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class LoginPresenter(private val sharePrefsManager: SharePrefsManager, val validator: Validator = Validator()) : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {
    override fun handleLogin(request: Login.Request) {
        mCompositeSubscription += ApiClient.omiseGO.login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView?.showLoading() }
                .doFinally { mView?.hideLoading() }
                .subscribe({
                    sharePrefsManager.saveLoginResponse(it.data)
                    mView?.showLoginSuccess(it.data)
                    log(it.toString())
                }, {
                    mView?.showMessage(it.errorResponse().data.description)
                    mView?.showLoginFailed(it.errorResponse().data)
                })
    }

    override fun handleClickRegisterButton() {
        mView?.showRegister()
    }

    override fun checkHasLogin() {
        val response = sharePrefsManager.readLoginResponse()
        if (response.userId.isNotEmpty()) {
            mView?.showLoginSuccess(response)
        }
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
}