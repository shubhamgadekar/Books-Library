package com.alpha.data

import com.alpha.myplatformdoor.FeatureCommand
import com.alpha.myplatformdoor.FeatureEntry
import com.alpha.myplatformdoor.FeatureResult
import com.alpha.myplatformdoor.MessageBus
import com.google.gson.JsonObject
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Singleton
class DataDoor @Inject constructor() : FeatureEntry {

    @Inject
    internal lateinit var bus: MessageBus

    override fun handle(command: FeatureCommand): Flow<FeatureResult> = flow {
        val response = JsonObject().apply {
            addProperty("status", "Domain handled")
        }

        bus.publish(FeatureCommand(messageName = "GetStudentsData", response))

        emit(
            FeatureResult.Success(
                FeatureCommand(
                    messageName = command.messageName,
                    payload = response
                )
            )
        )
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
//        scope.launch {
//            delay(1000)
//            bus.messages.collectLatest { message ->
//                when (message.messageName) {
//                    "getStudentsData" -> handleGetStudentsData()
//                }
//            }
//        }
    }

    private suspend fun handleGetStudentsData() {
        val response = JsonObject().apply {
            addProperty("status", "Domain handled")
        }
        // Publishing to the bus is fine,
        // but this is now clearly separated from listening.
        bus.publish(FeatureCommand("fetchedStudentsData", response))
    }
}
