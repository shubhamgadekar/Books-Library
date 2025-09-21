package com.alpha.modulesDoor

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Door @Inject constructor() {

    fun init(applicationContext: Context, doorInitializer: DoorInitializer) {
        val doorKernel = DoorKernel()
        doorKernel.init(applicationContext, doorInitializer)
    }
}
