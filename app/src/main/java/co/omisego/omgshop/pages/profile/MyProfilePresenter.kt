package co.omisego.omgshop.pages.profile

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.OMGApiManager
import co.omisego.omisego.model.Balance

class MyProfilePresenter(private val sharePrefsManager: SharePrefsManager) : BasePresenterImpl<MyProfileContract.View>(), MyProfileContract.Presenter {
    private val authToken by lazy {
        sharePrefsManager.loadCredential().omisegoAuthenticationToken
    }

    override fun loadSettings() {
        mView?.showLoading()
        OMGApiManager.listBalances(authToken, {
            mView?.showMessage(it.data.description)
            mView?.hideLoading()
        }) { response ->
            // If user doesn't select default minted token, then set default value to the first item.
            var selectedToken = getCurrentToken()

            selectedToken = response.data.data[0].balances.firstOrNull { it.mintedToken.id == selectedToken?.mintedToken?.id } ?: response.data.data[0].balances[0]
            saveSelectedToken(selectedToken)

            // set selected token id
            mView?.setCurrentSelectedTokenId(selectedToken.mintedToken.id)

            // update UI
            val balances = response.data.data.flatMap { it.balances }
            mView?.showBalances(balances)
            mView?.hideLoading()
        }
    }

    override fun saveSelectedToken(balance: Balance) {
        sharePrefsManager.saveSelectedTokenBalance(balance)
        mView?.setCurrentSelectedTokenId(balance.mintedToken.id)
    }

    override fun logout() {
        mView?.showLoadingDialog()
        OMGApiManager.logout(authToken, {
            mView?.showMessage(it.data.description)
            mView?.hideLoadingDialog()
        }) {
            sharePrefsManager.saveCredential(Credential("", "", ""))
            sharePrefsManager.saveSelectedTokenBalance(null)
            mView?.hideLoadingDialog()
            mView?.showLogout()
        }
    }

    override fun getCurrentToken(): Balance? {
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