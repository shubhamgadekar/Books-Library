package com.alpha.modulesDoor

import com.google.gson.JsonObject

data class DoorCommand(
    val messageName: String,
    val payload: JsonObject? = null,
    val doorName: String? = null
)
