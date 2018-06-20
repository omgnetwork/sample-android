package co.omisego.omgshop.pages.login

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.pages.login.caller.LoginCallerContract

interface LoginContract {
    interface View : BaseContract.BaseView {
        fun showLoginSuccess(response: Credential)
        fun showLoginFailed(response: Error)
        fun showRegister()
        fun showPasswordErrorHint(msg: String)
        fun showEmailErrorHint(msg: String)
    }

    interface Presenter : BaseContract.BasePresenter<View, LoginCallerContract.Caller> {
        fun checkHasLogin()
        fun handleClickRegisterButton()
        fun validateEmail(email: String): Boolean
        fun validatePassword(password: String): Boolean
    }
}