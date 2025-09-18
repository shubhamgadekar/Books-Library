package com.alpha.myplatformdoor

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface FeatureEntry {
    fun handle(command: FeatureCommand): Flow<FeatureResult>
}
