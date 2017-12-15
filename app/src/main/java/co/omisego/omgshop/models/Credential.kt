package co.omisego.omgshop.models

import co.omisego.omgshop.helpers.Constants
import com.google.gson.annotations.SerializedName


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

data class Credential(@SerializedName(Constants.REQUEST_KEY_USER_ID) val userId: String,
                      @SerializedName(Constants.REQUEST_KEY_AUTHENTICATION_TOKEN) val authenticationToken: String,
                      @SerializedName(Constants.REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN) val omisegoAuthenticationToken: String)