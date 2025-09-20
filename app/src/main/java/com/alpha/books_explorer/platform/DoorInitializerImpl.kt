package com.alpha.books_explorer.platform

import android.content.Context
import com.alpha.books_explorer.MainAppDoor
import com.alpha.data.DataDoor
import com.alpha.myplatformdoor.DoorInitializer
import com.alpha.myplatformdoor.FeatureEntry
import com.alpha.myplatformdoor.PlatformHub
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DoorInitializerImpl @Inject constructor(
    private val context: Context,
    private val dataDoor: DataDoor,
    private val mainAppDoor: MainAppDoor,
    private val platformHub: PlatformHub,
) : DoorInitializer {

    override val doorList: List<FeatureEntry>
        get() = listOf(
            dataDoor, mainAppDoor
        )

    override fun setup() {
        platformHub.init(context, this)
    }
}
