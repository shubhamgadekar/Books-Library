package com.alpha.myplatformdoor

interface DoorInitializer {

    val doorList: List<FeatureEntry>

    fun setup()
}
