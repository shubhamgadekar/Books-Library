package com.alpha.myplatformdoor

interface DoorInitializer {

    val doorList: List<Pair<FeatureEntry, List<String>>>

    val doorEventList: List<Pair<Pair<FeatureEntry, List<String>>, FeatureEntry>>

    fun setup()
}
