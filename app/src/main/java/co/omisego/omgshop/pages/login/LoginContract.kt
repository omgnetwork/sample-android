package co.omisego.omgshop.pages.login

import co.omisego.omgshop.base.BaseContract
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
        fun showLoginSuccess(response: Login.Response)
        fun showLoginFailed(response: Error)
        fun showRegister()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun handleLogin(request: Login.Request)
        fun handleClickRegisterButton()
    }
}