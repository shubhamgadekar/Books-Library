package com.alpha.myplatformdoor

import android.content.Context
import com.alpha.myplatformdoor.messageSender.MessageSender
import com.alpha.myplatformdoor.messageTypes.EventType
import com.alpha.myplatformdoor.messageTypes.MessageType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

abstract class FeatureEntry {

    lateinit var messageSender: MessageSender
        private set

    abstract val messageList: List<MessageType>

    abstract val eventList: List<EventType>

    abstract fun handle(command: FeatureCommand): Flow<FeatureCommand>

    abstract fun init(context: Context)

    abstract fun onReceive(message: FeatureCommand)

    abstract fun publish(message: FeatureCommand): SharedFlow<FeatureCommand>

    abstract fun subscribe(subscription: SharedFlow<FeatureCommand>, featureCommand: FeatureCommand)

    val name: String = javaClass.simpleName

    fun init(
        context: Context,
        messageSender: MessageSender
    ) {
        this.messageSender = messageSender
    }
}
