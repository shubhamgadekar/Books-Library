package com.alpha.modulesDoor

import android.content.Context
import com.alpha.modulesDoor.messageSender.MessageSenderImpl
import com.alpha.modulesDoor.messageTypes.EventType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

internal class DoorKernel(
    private val messageSender: MessageSenderImpl = MessageSenderImpl(),
) {

    private val eventPublishers = mutableMapOf<String, SharedFlow<DoorCommand>>()

    fun init(
        applicationContext: Context,
        doorInitializer: DoorInitializer,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            messageSender.init(doorInitializer)

            doorInitializer.doorList.forEach { door ->
                door.init(applicationContext, messageSender)
            }

            subscribeEventByDoors(doorInitializer)
        }
    }

    fun subscribeEventByDoors(doorInitializer: DoorInitializer) {
        doorInitializer.doorList.forEach { door ->
            door.eventList.forEach { event ->
                when (event) {
                    is EventType.PublishType -> {
                        val message = DoorCommand(messageName = event.eventName, doorName = event.door.name)
                        val flow = door.publish(message)
                        eventPublishers[event.eventName] = flow
                    }

                    is EventType.SubscribeType -> {
                        // Do nothing here
                    }
                }
            }
        }

        doorInitializer.doorList.forEach { door ->
            door.eventList.forEach { event ->
                when (event) {
                    is EventType.PublishType -> {
                        // Do nothing here
                    }

                    is EventType.SubscribeType -> {
                        if (eventPublishers[event.eventName] == null) {
                            throw Exception("${event.eventName} - this event is not registered, please do check your event list once")
                        } else {
                            event.door.subscribe(eventPublishers[event.eventName]!!, DoorCommand(event.eventName))
                        }
                    }
                }
            }
        }
    }
}
