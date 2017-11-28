package co.omisego.omgshop.testutils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

fun Any.readFile(name: String): File = File(javaClass.classLoader.getResource(name).path)
