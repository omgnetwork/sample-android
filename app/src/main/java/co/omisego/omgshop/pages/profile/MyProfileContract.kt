package co.omisego.omgshop.pages.profile

import co.omisego.androidsdk.models.Balance
import co.omisego.omgshop.base.BaseContract


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface MyProfileContract {
    interface View : BaseContract.BaseView {
        fun showBalances(listBalance: List<Balance>)
        fun setCurrentSelectedTokenId(id: String)
        fun showUsername(email: String)
        fun showLogout()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadSettings()
        fun loadUser()
        fun saveSelectedMintedToken(id: String)
        fun logout()
        fun getCurrentMintedTokenId(): String
    }
}