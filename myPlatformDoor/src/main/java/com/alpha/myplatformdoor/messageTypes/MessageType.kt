package com.alpha.myplatformdoor.messageTypes

import com.alpha.myplatformdoor.FeatureEntry

sealed class MessageType() {
    data class SendType(val messageName: String, val door: FeatureEntry) : MessageType()
    data class ReceiveType(val messageName: String, val door: FeatureEntry) : MessageType()
}
