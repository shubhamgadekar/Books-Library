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
internal class PlatformModule {

    @Provides
    @Singleton
    internal fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    internal fun providePlatformHub(): PlatformHub {
        return PlatformHub()
    }

    @Provides
    @Singleton
    internal fun provideMessageBus(): MessageBus = MessageBus()

    @Provides
    @Singleton
    internal fun providesDoorInitializer(
        @ApplicationContext context: Context,
        dataDoor: DataDoor,
        mainAppDoor: MainAppDoor,
        platformHub: PlatformHub,
    ): DoorInitializer {
        return DoorInitializerImpl(context, dataDoor, mainAppDoor, platformHub)
    }
}
