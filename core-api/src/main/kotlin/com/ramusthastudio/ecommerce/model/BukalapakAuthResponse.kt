package com.ramusthastudio.ecommerce.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class BukalapakAuthResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("access_token") val accessToken: String,
    @JsonNames("expires_at") val expiresAt: String
)
