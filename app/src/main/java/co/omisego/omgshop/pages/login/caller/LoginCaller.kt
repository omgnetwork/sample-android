package co.omisego.omgshop.pages.login.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.network.CombinedAPIManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class LoginCaller(
    private val handler: LoginCallerContract.Handler
) : BaseCaller(), LoginCallerContract.Caller {

    override fun login(request: Login.Request) {
        handler.showLoading()
        mCompositeSubscription += CombinedAPIManager.login(request)
            .doOnSubscribe { handler.showLoading() }
            .subscribe(handler::handleLoginSuccess, handler::handleLoginFailed)
    }
}
