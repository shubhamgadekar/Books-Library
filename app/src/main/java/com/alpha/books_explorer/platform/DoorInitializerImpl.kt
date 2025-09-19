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
class DoorInitializerImpl @Inject constructor(
    private val context: Context,
    private val dataDoor: DataDoor,
    private val mainAppDoor: MainAppDoor,
    private val platformHub: PlatformHub,
) : DoorInitializer {

    override val doorList: List<Pair<FeatureEntry, List<String>>>
        get() = listOf(
            Pair(dataDoor, listOf("GetBookById", "GetBookList", "GetReadingList", "GetSearchResult", "GetFavList")),
            Pair(mainAppDoor, listOf("ReceivedBookByIdResponse", "ReceivedBookListResponse")),
        )

    override val doorEventList: List<Pair<Pair<FeatureEntry, List<String>>, FeatureEntry>>
        get() = listOf(
            Pair(
                Pair(
                    dataDoor,
                    listOf("SubscribeBookById", "SubscribeReadingList", "SubscribeSearchResult", "SubscribeFavList")
                ),
                mainAppDoor
            ),
        )

    override fun setup() {
        platformHub.init(context, this)
    }
}
