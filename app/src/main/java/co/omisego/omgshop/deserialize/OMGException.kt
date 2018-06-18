package co.omisego.omgshop.deserialize

import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Response

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class OMGException(val error: Response<Error>) : Exception() {
    override val message: String = error.toString()
}