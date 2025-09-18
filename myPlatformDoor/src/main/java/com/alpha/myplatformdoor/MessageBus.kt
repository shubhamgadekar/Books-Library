package com.alpha.myplatformdoor

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Singleton
class MessageBus @Inject constructor() {
    private val _messages = MutableSharedFlow<FeatureCommand>()
    val messages: SharedFlow<FeatureCommand> = _messages.asSharedFlow()

    suspend fun publish(message: FeatureCommand) {
        _messages.emit(message)
    }
}
