package com.alpha.myplatformdoor

import android.content.Context
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

abstract class FeatureEntry {

    lateinit var messageSender: MessageSender
        private set

    abstract fun handle(command: FeatureCommand): Flow<FeatureResult>

    abstract fun init(context: Context)

    abstract fun onReceive(message: FeatureCommand)

    abstract fun publish(message: FeatureCommand): SharedFlow<FeatureCommand>

    abstract fun subscribe(subscription: SharedFlow<FeatureCommand>, featureCommand: FeatureCommand)

    fun init(
        context: Context,
        messageSender: MessageSender
    ) {
        this.messageSender = messageSender
    }
}
