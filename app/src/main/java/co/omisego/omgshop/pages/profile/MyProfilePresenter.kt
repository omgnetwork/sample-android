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
import co.omisego.omisego.model.User
import co.omisego.omisego.model.WalletList

class MyProfilePresenter : BasePresenterImpl<MyProfileContract.View, MyProfileCallerContract.Caller>(),
    MyProfileContract.Presenter,
    MyProfileCallerContract.Handler {
    override var caller: MyProfileCallerContract.Caller? = MyProfileCaller(this)

    override fun saveSelectedToken(balance: Balance) {
        Preference.saveSelectedTokenBalance(balance)
        mView?.setCurrentSelectedTokenId(balance.token.id)
    }

    override fun getCurrentToken(): Balance? {
        return Preference.loadSelectedTokenBalance()
    }

    override fun handleLoadWalletSuccess(response: OMGResponse<WalletList>) {
        mView?.hideLoading()
        // If user doesn't select default minted token, then set default value to the first item.
        var selectedToken = getCurrentToken()

        selectedToken = response.data.data[0].balances.firstOrNull { it.token.id == selectedToken?.token?.id } ?: response.data.data[0].balances[0]
        saveSelectedToken(selectedToken)

        // set selected token id
        mView?.setCurrentSelectedTokenId(selectedToken.token.id)

        // update UI
        val balances = response.data.data.flatMap { it.balances }
        mView?.showBalances(balances)
    }

    override fun handleLoadWalletFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showMessage(error.data.description)
    }

    override fun handleLogoutFailed(error: OMGResponse<APIError>) {
        mView?.hideLoadingDialog()
        mView?.showMessage(error.data.description)
    }

    override fun handleLogoutSuccess(response: OMGResponse<Logout>) {
        mView?.hideLoadingDialog()
        Preference.saveCredential(Credential("", "", ""))
        Preference.saveSelectedTokenBalance(null)
        mView?.showLogout()
    }

    override fun handleLoadUserSuccess(response: OMGResponse<User>) {
        mView?.hideLoading()
        mView?.showUsername(response.data.username)
        caller?.loadWallets()
    }

    override fun handleLoadUserFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showMessage(error.data.description)
        caller?.loadWallets()
    }

    override fun showLoading(dialog: Boolean) {
        if (dialog) mView?.showLoadingDialog()
        else mView?.showLoading()
    }
}