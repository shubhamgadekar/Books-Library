package com.alpha.myplatformdoor

internal class MessageSenderImpl : MessageSender {

    private val messageReceiver = mutableMapOf<String, FeatureEntry>()

    private val eventReceiver = mutableMapOf<String, FeatureEntry>()

    fun init(doorInitializer: DoorInitializer) {
        doorInitializer.doorList.forEach { door ->
            door.second.forEach { messageName ->
                messageReceiver[messageName] = door.first
            }
        }

        doorInitializer.doorEventList.forEach { door ->
            door.first.second.forEach { messageName ->
                eventReceiver[messageName] = door.first.first
            }
        }
    }

    override fun send(featureCommand: FeatureCommand) {
        messageReceiver[featureCommand.messageName]?.onReceive(featureCommand)
    }
}
