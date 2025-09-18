package com.alpha.myplatformdoor

import kotlin.reflect.KClass

object FeatureRegistry {
    private val entries = mutableMapOf<KClass<*>, FeatureEntry>()

    fun <T : FeatureEntry> register(clazz: KClass<T>, entry: T): T {
        entries[clazz] = entry
        return entry
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : FeatureEntry> require(clazz: KClass<T>): T {
        return entries[clazz] as? T
            ?: throw IllegalStateException("FeatureEntry not registered: ${clazz.simpleName}")
    }

    fun all(): Collection<FeatureEntry> = entries.values
}
