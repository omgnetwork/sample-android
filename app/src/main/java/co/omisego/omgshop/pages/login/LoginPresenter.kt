package co.omisego.omgshop.pages.login

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.models.Login


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class LoginPresenter : BasePresenterImpl<LoginContract.View>(), LoginContract.Presenter {
    override fun handleLogin(request: Login.Request) {

    }

    override fun handleClickRegisterButton() {

    }
}