package co.omisego.omgshop.pages.profile.delegator

import co.omisego.omgshop.pages.profile.viewholder.BalanceListener
import co.omisego.omgshop.pages.profile.viewholder.BalanceListenerHolder

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class BalanceListenerDelegator : BalanceListenerHolder {
    override var balanceListener: BalanceListener? = null
}
