package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.helpers.Constants
import com.google.gson.annotations.SerializedName

data class Credential(@SerializedName(Constants.REQUEST_KEY_USER_ID) val userId: String,
                      @SerializedName(Constants.REQUEST_KEY_AUTHENTICATION_TOKEN) val authenticationToken: String,
                      @SerializedName(Constants.REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN) val omisegoAuthenticationToken: String)