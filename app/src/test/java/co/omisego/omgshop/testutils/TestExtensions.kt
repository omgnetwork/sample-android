package co.omisego.omgshop.testutils

import java.io.File

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun Any.readFile(name: String): File = File(javaClass.classLoader.getResource(name).path)
