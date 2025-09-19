package com.alpha.myplatformdoor

interface MessageSender {
    fun send(featureCommand: FeatureCommand)
}
