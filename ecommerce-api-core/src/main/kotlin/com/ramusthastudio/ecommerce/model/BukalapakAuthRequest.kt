package com.ramusthastudio.ecommerce.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BukalapakAuthRequest(
    @SerialName("application_id") val applicationId: Int = 1,
    @SerialName("authenticity_token") val authenticityToken: String = ""
)

