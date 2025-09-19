package com.alpha.myplatformdoor

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class MicroKernel(
    private val messageSender: MessageSenderImpl = MessageSenderImpl(),
) {

    fun init(
        applicationContext: Context,
        doorInitializer: DoorInitializer,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            messageSender.init(doorInitializer)

            doorInitializer.doorList.forEach { door ->
                door.first.init(context = applicationContext, messageSender = messageSender)
            }

            subscribeAllEvents(doorInitializer, applicationContext)
        }
    }

    fun subscribeEvent(doorList: Pair<FeatureEntry, List<String>>, publisherDoor: FeatureEntry) {
        val doorPlugin = doorList.first
        doorList.second.forEach { door ->
            val message = FeatureCommand(door)
            val flow = doorPlugin.publish(message)
            publisherDoor.subscribe(flow, message)
        }
    }

    fun subscribeAllEvents(doorInitializer: DoorInitializer, applicationContext: Context) {
        doorInitializer.doorEventList.forEach { door ->
            val publisherDoor = door.second
            subscribeEvent(door.first, publisherDoor)
        }
    }
}
