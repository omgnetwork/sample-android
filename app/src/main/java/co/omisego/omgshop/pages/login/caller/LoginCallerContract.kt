package co.omisego.omgshop.pages.login.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.models.Response

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface LoginCallerContract {
    interface Caller : BaseContract.BaseCaller {
        fun login(request: Login.Request)
    }

    interface Handler {
        fun handleLoginSuccess(response: Response<Credential>)
        fun handleLoginFailed(error: Throwable)
        fun showLoading()
    }
}