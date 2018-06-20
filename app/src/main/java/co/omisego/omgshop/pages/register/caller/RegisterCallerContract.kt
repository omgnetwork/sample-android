package co.omisego.omgshop.pages.register.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Register
import co.omisego.omgshop.models.Response

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface RegisterCallerContract {
    interface Caller : BaseContract.BaseCaller {
        fun register(request: Register.Request)
    }

    interface Handler {
        fun handleRegisterSuccess(response: Response<Credential>)
        fun handleRegisterFailed(error: Throwable)
        fun showLoading()
    }
}