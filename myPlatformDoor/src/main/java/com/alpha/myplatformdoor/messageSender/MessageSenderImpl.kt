package com.alpha.myplatformdoor.messageSender

import com.alpha.myplatformdoor.DoorInitializer
import com.alpha.myplatformdoor.FeatureCommand
import com.alpha.myplatformdoor.FeatureEntry
import com.alpha.myplatformdoor.messageTypes.MessageType

internal class MessageSenderImpl : MessageSender {

    private val messageReceiver = mutableMapOf<String, FeatureEntry>()

    private val messageSender = mutableMapOf<String, FeatureEntry>()

    fun init(doorInitializer: DoorInitializer) {
        doorInitializer.doorList.forEach { door ->
            door.messageList.forEach { message ->
                when (message) {
                    is MessageType.ReceiveType -> {
                        messageReceiver[message.messageName] = door
                    }

                    is MessageType.SendType -> {
                        messageSender[message.messageName] = door
                    }
                }
            }
        }
    }

    override fun send(featureCommand: FeatureCommand) {
        if (messageSender[featureCommand.messageName]?.name != featureCommand.doorName) {
            throw Exception("${featureCommand.messageName} - this message is not registered, please do check your message list once from ${featureCommand.doorName}")
        } else {
            messageReceiver[featureCommand.messageName]?.onReceive(featureCommand)
        }
    }
}
