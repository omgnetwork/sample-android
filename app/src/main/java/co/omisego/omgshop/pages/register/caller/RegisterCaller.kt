package co.omisego.omgshop.pages.register.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.models.Register
import co.omisego.omgshop.network.CombinedAPIManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class RegisterCaller(
    private val handler: RegisterCallerContract.Handler
) : BaseCaller(), RegisterCallerContract.Caller {

    override fun register(request: Register.Request) {
        handler.showLoading()
        mCompositeSubscription += CombinedAPIManager.register(request)
            .doOnSubscribe { handler.showLoading() }
            .subscribe(handler::handleRegisterSuccess, handler::handleRegisterFailed)
    }
}
