package com.alpha.myplatformdoor

import com.google.gson.JsonObject

data class FeatureCommand(
    val messageName: String,
    val payload: JsonObject? = null
)
