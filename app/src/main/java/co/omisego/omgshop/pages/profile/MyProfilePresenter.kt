package co.omisego.omgshop.pages.profile

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.pages.profile.caller.MyProfileCaller
import co.omisego.omgshop.pages.profile.caller.MyProfileCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Logout
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList

class MyProfilePresenter : BasePresenterImpl<MyProfileContract.View, MyProfileCallerContract.Caller>(),
    MyProfileContract.Presenter,
    MyProfileCallerContract.Handler {
    override var caller: MyProfileCallerContract.Caller? = MyProfileCaller(this)

    override fun saveSelectedBalance(balance: Balance) {
        Preference.saveSelectedTokenBalance(balance)
    }

    override fun getCurrentBalance(): Balance? {
        return Preference.loadSelectedTokenBalance()
    }

    override fun handleLoadWalletSuccess(response: OMGResponse<WalletList>) {

        var currentBalance = response.data.data[0].balances.firstOrNull { it.token.id == getCurrentBalance()?.token?.id }
        if (currentBalance == null) {
            currentBalance = response.data.data[0].balances[0]
            saveSelectedBalance(currentBalance)
        }
        mView?.setSelectedBalance(currentBalance)

        // Save current address
        Preference.saveWalletAddress(response.data.data[0].address)

        // update UI
        val balances = response.data.data.flatMap { it.balances }
        mView?.showBalances(balances, currentBalance)
        mView?.showUsername(response.data.data[0].user?.username?.split("|")?.get(0)
            ?: "Cannot found the user")
    }

    override fun handleLoadWalletFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showMessage(error.data.description)
        mView?.showLoadBalanceFailed()
        goBackToLoginIfNeeded(error.data)
    }

    override fun handleLogoutFailed(error: OMGResponse<APIError>) {
        mView?.hideLoadingDialog()
        mView?.showMessage(error.data.description)
        goBackToLoginIfNeeded(error.data)
    }

    override fun handleLogoutSuccess(response: OMGResponse<Logout>) {
        mView?.hideLoadingDialog()
        Preference.saveCredential(Credential("", "", ""))
        Preference.saveSelectedTokenBalance(null)
        mView?.showLogout()
    }

    override fun showLoading(dialog: Boolean) {
        if (dialog) mView?.showLoadingDialog()
        else mView?.showLoading()
    }
}