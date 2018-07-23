package co.omisego.omgshop.pages.profile.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.CombinedAPIManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class MyProfileCaller(
    private val handler: MyProfileCallerContract.Handler,
    override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), MyProfileCallerContract.Caller {
    override fun loadWallets(authToken: String) {
        CombinedAPIManager.getWallets(authToken, handler::handleLoadWalletFailed, handler::handleLoadWalletSuccess)
    }

    override fun logout(authToken: String) {
        handler.showLoading(dialog = true)
        CombinedAPIManager.logout(authToken, handler::handleLogoutFailed, handler::handleLogoutSuccess)
    }
}
