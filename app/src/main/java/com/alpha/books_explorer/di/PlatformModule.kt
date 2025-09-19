package com.alpha.books_explorer.di

import android.content.Context
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.platform.DoorInitializerImpl
import com.alpha.data.DataDoor
import com.alpha.myplatformdoor.DoorInitializer
import com.alpha.myplatformdoor.MessageBus
import com.alpha.myplatformdoor.PlatformHub
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlatformModule {

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

//    @Provides
//    @Singleton
//    fun providesDataDoor(): DataDoor {
//        return DataDoor()
//    }

//    @Provides
//    @Singleton
//    fun providesAppDoor(): MainAppDoor {
//        return MainAppDoor()
//    }

    @Provides
    @Singleton
    fun providePlatformHub(): PlatformHub {
        return PlatformHub()
    }

    @Provides
    @Singleton
    fun provideMessageBus(): MessageBus = MessageBus()

    @Provides
    @Singleton
    fun providesDoorInitializer(
        @ApplicationContext context: Context,
        dataDoor: DataDoor,
        mainAppDoor: MainAppDoor,
        platformHub: PlatformHub,
    ): DoorInitializer {
        return DoorInitializerImpl(context, dataDoor, mainAppDoor, platformHub)
    }
}
