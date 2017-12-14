package co.omisego.omgshop.pages.profile

import co.omisego.androidsdk.models.Balance
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.network.OMGApiManager


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class MyProfilePresenter(private val sharePrefsManager: SharePrefsManager) : BasePresenterImpl<MyProfileContract.View>(), MyProfileContract.Presenter {
    private val authToken by lazy {
        sharePrefsManager.readLoginResponse().omisegoAuthenticationToken
    }

    override fun loadSettings() {
        mView?.showLoading()
        OMGApiManager.listBalances(authToken, {
            mView?.showMessage(it.data.description)
            mView?.hideLoading()
        }) { response ->
            // If user doesn't select default minted token, then set default value to the first item.
            var selectedTokenId = getCurrentBalance()?.mintedToken?.id ?: ""
            if (selectedTokenId.isEmpty()) {
                val balance = response.data[0].balances[0]
                saveSelectedBalance(balance)
                selectedTokenId = balance.mintedToken.id
            }

            // set selected token id
            mView?.setCurrentSelectedTokenId(selectedTokenId)

            // update UI
            val balances = response.data.flatMap { it.balances }
            mView?.showBalances(balances)
            mView?.hideLoading()
        }
    }

    override fun saveSelectedBalance(balance: Balance) {
        sharePrefsManager.saveSelectedTokenBalance(balance)
        mView?.setCurrentSelectedTokenId(balance.mintedToken.id)
    }

    override fun logout() {
        sharePrefsManager.saveLoginResponse(Login.Response("", "", ""))
        sharePrefsManager.saveSelectedTokenBalance(null)
        mView?.showLogout()
    }

    override fun getCurrentBalance(): Balance? {
        return sharePrefsManager.loadSelectedTokenBalance()
    }

    override fun loadUser() {
        OMGApiManager.loadUser(authToken, {
            mView?.showMessage(it.data.description)
        }) {
            mView?.showUsername(it.data.username)
        }
    }
}