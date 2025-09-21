package com.alpha.books_explorer.platform

import android.content.Context
import com.alpha.books_explorer.MainAppDoor
import com.alpha.data.DataDoor
import com.alpha.modulesDoor.DoorInitializer
import com.alpha.modulesDoor.DoorEntry
import com.alpha.modulesDoor.Door
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DoorInitializerImpl @Inject constructor(
    private val context: Context,
    private val dataDoor: DataDoor,
    private val mainAppDoor: MainAppDoor,
    private val door: Door,
) : DoorInitializer {

    override val doorList: List<DoorEntry>
        get() = listOf(
            dataDoor, mainAppDoor
        )

    override fun setup() {
        door.init(context, this)
    }
}
