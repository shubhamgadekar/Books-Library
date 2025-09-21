package com.alpha.books_explorer

import android.app.Application
import com.alpha.modulesDoor.DoorInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BooksExplorerApplication : Application() {

    @Inject
    lateinit var doorInitializer: DoorInitializer

    override fun onCreate() {
        super.onCreate()
        doorInitializer.setup()
    }
}
