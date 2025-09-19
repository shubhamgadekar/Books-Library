package com.alpha.myplatformdoor

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlatformHub @Inject constructor() {

    fun init(applicationContext: Context, doorInitializer: DoorInitializer) {
        val microKernel = MicroKernel()
        microKernel.init(applicationContext, doorInitializer)
    }
}
