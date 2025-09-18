package com.alpha.myplatformdoor

object MessageRegistry {
    private val registered = mutableSetOf<String>()

    fun register(messages: List<String>) {
        registered.addAll(messages)
    }

    fun isRegistered(message: String): Boolean {
        return registered.contains(message)
    }

    fun all(): Set<String> = registered
}
