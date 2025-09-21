package com.alpha.modulesDoor.messageTypes

import com.alpha.modulesDoor.DoorEntry

sealed class MessageType() {
    data class SendType(val messageName: String, val door: DoorEntry) : MessageType()
    data class ReceiveType(val messageName: String, val door: DoorEntry) : MessageType()
}
