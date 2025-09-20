package com.alpha.myplatformdoor.messageTypes

import com.alpha.myplatformdoor.FeatureEntry

sealed class EventType() {
    data class PublishType(val eventName: String, val door: FeatureEntry) : EventType()
    data class SubscribeType(val eventName: String, val door: FeatureEntry) : EventType()
}
