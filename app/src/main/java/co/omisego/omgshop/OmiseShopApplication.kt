package co.omisego.omgshop

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import co.omisego.omgshop.helpers.Config
import co.omisego.omgshop.helpers.Contextor
import com.facebook.stetho.Stetho

class OmiseShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Config.init(applicationContext)
        Contextor.context = applicationContext
        Stetho.initializeWithDefaults(this)
    }
}