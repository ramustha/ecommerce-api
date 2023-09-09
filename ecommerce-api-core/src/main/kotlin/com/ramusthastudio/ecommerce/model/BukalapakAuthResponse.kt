package com.ramusthastudio.ecommerce.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BukalapakAuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_at") val expiresAt: String
)
