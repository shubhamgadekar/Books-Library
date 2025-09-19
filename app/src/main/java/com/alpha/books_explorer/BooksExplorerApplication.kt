package com.alpha.books_explorer

//import com.alpha.domain.DomainDoor
import android.app.Application
import com.alpha.books_explorer.platform.Messages
import com.alpha.data.DataDoor
import com.alpha.myplatformdoor.DoorInitializer
import com.alpha.myplatformdoor.FeatureRegistry
import com.alpha.myplatformdoor.MessageRegistry
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BooksExplorerApplication : Application() {

//    @Inject
//    lateinit var dataDoor: DataDoor

    @Inject
    lateinit var doorInitializer: DoorInitializer

    override fun onCreate() {
        super.onCreate()

//        MessageRegistry.register(Messages.entries.map { it.name })
//        FeatureRegistry.register(DataDoor::class, dataDoor)
        doorInitializer.setup()
    }
}
