package com.ramusthastudio.ecommerce.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class BukalapakAuthRequest @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("application_id") val applicationId: Int = 1,
    @JsonNames("authenticity_token") val authenticityToken: String = ""
)

