package com.alpha.modulesDoor.messageSender

import com.alpha.modulesDoor.DoorInitializer
import com.alpha.modulesDoor.DoorCommand
import com.alpha.modulesDoor.DoorEntry
import com.alpha.modulesDoor.messageTypes.MessageType

internal class MessageSenderImpl : MessageSender {

    private val messageReceiver = mutableMapOf<String, DoorEntry>()

    private val messageSender = mutableMapOf<String, DoorEntry>()

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

    override fun send(doorCommand: DoorCommand) {
        if (messageSender[doorCommand.messageName]?.name != doorCommand.doorName) {
            throw Exception("${doorCommand.messageName} - this message is not registered, please do check your message list once from ${doorCommand.doorName}")
        } else {
            messageReceiver[doorCommand.messageName]?.onReceive(doorCommand)
        }
    }
}
