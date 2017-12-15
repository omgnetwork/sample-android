package co.omisego.omgshop.pages.register

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Register


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface RegisterContract {
    interface View : BaseContract.BaseView {
        fun showRegisterSuccess(response: Credential)
        fun showRegisterFailed(response: Error)
        fun showPasswordErrorHint(msg: String)
        fun showFirstNameErrorHint(msg: String)
        fun showLastNameErrorHint(msg: String)
        fun showEmailErrorHint(msg: String)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun handleRegister(request: Register.Request)
        fun validateEmail(email: String): Boolean
        fun validatePassword(password: String): Boolean
        fun validateFirstName(firstName: String): Boolean
        fun validateLastName(lastName: String): Boolean
    }
}