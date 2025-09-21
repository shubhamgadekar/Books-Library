package com.alpha.books_explorer.di

import android.content.Context
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.platform.DoorInitializerImpl
import com.alpha.data.DataDoor
import com.alpha.modulesDoor.DoorInitializer
import com.alpha.modulesDoor.Door
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DoorModule {

    @Provides
    @Singleton
    internal fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    internal fun providePlatformHub(): Door {
        return Door()
    }

    @Provides
    @Singleton
    internal fun providesDoorInitializer(
        @ApplicationContext context: Context,
        dataDoor: DataDoor,
        mainAppDoor: MainAppDoor,
        door: Door,
    ): DoorInitializer {
        return DoorInitializerImpl(context, dataDoor, mainAppDoor, door)
    }
}
