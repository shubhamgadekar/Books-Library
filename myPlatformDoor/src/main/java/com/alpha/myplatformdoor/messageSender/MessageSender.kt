package com.alpha.myplatformdoor.messageSender

import com.alpha.myplatformdoor.FeatureCommand

interface MessageSender {
    fun send(featureCommand: FeatureCommand)
}
