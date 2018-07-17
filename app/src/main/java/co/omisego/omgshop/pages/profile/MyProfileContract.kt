package co.omisego.omgshop.pages.profile

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.profile.caller.MyProfileCallerContract
import co.omisego.omisego.model.Balance

interface MyProfileContract {
    interface View : BaseContract.BaseView {
        fun showBalances(listBalance: List<Balance>, selectedBalance: Balance)
        fun setSelectedBalance(balance: Balance)
        fun showUsername(email: String)
        fun showLogout()
        fun showLoadBalanceFailed()
        fun showLoadingDialog()
        fun hideLoadingDialog()
    }

    interface Presenter : BaseContract.BasePresenter<View, MyProfileCallerContract.Caller> {
        fun saveSelectedBalance(balance: Balance)
        fun getCurrentBalance(): Balance?
    }
}