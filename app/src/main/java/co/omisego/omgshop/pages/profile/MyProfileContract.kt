package co.omisego.omgshop.pages.profile

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omisego.model.Balance

interface MyProfileContract {
    interface View : BaseContract.BaseView {
        fun showBalances(listBalance: List<Balance>)
        fun setCurrentSelectedTokenId(id: String)
        fun showUsername(email: String)
        fun showLogout()
        fun showLoadingDialog()
        fun hideLoadingDialog()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSettings()
        fun loadUser()
        fun saveSelectedToken(balance: Balance)
        fun logout()
        fun getCurrentToken(): Balance?
    }
}