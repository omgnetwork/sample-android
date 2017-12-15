package co.omisego.omgshop.pages.login

import co.omisego.androidsdk.models.ApiError
import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Login


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface LoginContract {
    interface View : BaseContract.BaseView {
        fun showLoginSuccess(response: Credential)
        fun showLoginFailed(response: Error)
        fun showRegister()
        fun showPasswordErrorHint(msg: String)
        fun showEmailErrorHint(msg: String)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun handleLogin(request: Login.Request)
        fun handleClickRegisterButton()
        fun checkHasLogin()
        fun validateEmail(email: String): Boolean
        fun validatePassword(password: String): Boolean
    }
}