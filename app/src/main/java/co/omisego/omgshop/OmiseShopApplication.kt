package co.omisego.omgshop

import android.app.Application
import co.omisego.omgshop.helpers.Contextor


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class OmiseShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Contextor.context = applicationContext
    }
}