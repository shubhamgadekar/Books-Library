package com.alpha.myplatformdoor

import android.content.Context
import com.alpha.myplatformdoor.messageSender.MessageSenderImpl
import com.alpha.myplatformdoor.messageTypes.EventType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

internal class MicroKernel(
    private val messageSender: MessageSenderImpl = MessageSenderImpl(),
) {

    private val eventPublishers = mutableMapOf<String, SharedFlow<FeatureCommand>>()

    fun init(
        applicationContext: Context,
        doorInitializer: DoorInitializer,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            messageSender.init(doorInitializer)

            doorInitializer.doorList.forEach { door ->
                door.init(applicationContext, messageSender)
            }

//            subscribeAllEvents(doorInitializer, applicationContext)
            subscribeEventByDoors(doorInitializer)
        }
    }

    fun subscribeEventByDoors(doorInitializer: DoorInitializer) {
        doorInitializer.doorList.forEach { door ->
            door.eventList.forEach { event ->
                when (event) {
                    is EventType.PublishType -> {
                        val message = FeatureCommand(messageName = event.eventName, doorName = event.door.name)
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
                            event.door.subscribe(eventPublishers[event.eventName]!!, FeatureCommand(event.eventName))
                        }
                    }
                }
            }
        }
    }
}
