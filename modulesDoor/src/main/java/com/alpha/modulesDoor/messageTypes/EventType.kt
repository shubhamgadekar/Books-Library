package com.alpha.modulesDoor.messageTypes

import com.alpha.modulesDoor.DoorEntry

sealed class EventType() {
    data class PublishType(val eventName: String, val door: DoorEntry) : EventType()
    data class SubscribeType(val eventName: String, val door: DoorEntry) : EventType()
}
