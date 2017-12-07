package co.omisego.omgshop.pages.profile

import android.util.Base64
import co.omisego.androidsdk.Callback
import co.omisego.androidsdk.OMGApiClient
import co.omisego.androidsdk.models.Address
import co.omisego.androidsdk.models.ApiError
import co.omisego.androidsdk.models.Response
import co.omisego.androidsdk.models.User
import co.omisego.omgshop.BuildConfig
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Login


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class MyProfilePresenter(private val sharePrefsManager: SharePrefsManager) : BasePresenterImpl<MyProfileContract.View>(), MyProfileContract.Presenter {

    private val omgApiClient by lazy {
        val apiKey = BuildConfig.KUBERA_API_KEY
        val authToken = sharePrefsManager.readLoginResponse().omisegoAuthenticationToken
        val apiClientHeader = "OMGClient ${Base64.encodeToString("$apiKey:$authToken".toByteArray(), Base64.NO_WRAP)}"
        OMGApiClient.Builder {
            setAuthorizationToken(apiClientHeader)
        }.build()
    }

    override fun loadSettings() {
        mView?.showLoading()

        omgApiClient.listBalances(object : Callback<List<Address>> {
            override fun fail(response: Response<ApiError>) {
                mView?.showMessage(response.data.description)
                mView?.hideLoading()
                log(response.toString())
            }

            override fun success(response: Response<List<Address>>) {
                log(response.toString())

                // If user doesn't select default minted token, then set default value to the first item.
                var selectedToken = getCurrentMintedTokenId()
                if (selectedToken.isEmpty()) {
                    val id = response.data[0].balances[0].mintedToken.id
                    saveSelectedMintedToken(id)
                    selectedToken = id
                }

                // set selected token id
                mView?.setCurrentSelectedTokenId(selectedToken)

                // update UI
                val balances = response.data.flatMap { it.balances }
                mView?.showBalances(balances)
                mView?.hideLoading()
            }
        })
    }

    override fun saveSelectedMintedToken(id: String) {
        sharePrefsManager.saveMintedTokenId(id)
        mView?.setCurrentSelectedTokenId(id)
    }

    override fun logout() {
        sharePrefsManager.saveLoginResponse(Login.Response("", "", ""))
        sharePrefsManager.saveMintedTokenId("")
        mView?.showLogout()
    }

    override fun getCurrentMintedTokenId() = sharePrefsManager.loadMintedTokenId()

    override fun loadUser() {
        omgApiClient.getCurrentUser(object : Callback<User> {
            override fun fail(response: Response<ApiError>) {
                mView?.showMessage(response.data.description)
            }

            override fun success(response: Response<User>) {
                mView?.showUsername(response.data.username)
            }
        })
    }
}