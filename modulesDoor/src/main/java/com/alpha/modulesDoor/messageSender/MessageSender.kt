package com.alpha.modulesDoor.messageSender

import com.alpha.modulesDoor.DoorCommand

interface MessageSender {
    fun send(doorCommand: DoorCommand)
}
