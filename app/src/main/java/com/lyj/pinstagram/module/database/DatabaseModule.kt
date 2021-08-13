package com.lyj.pinstagram.module.database

import android.content.Context
import androidx.room.Room
import com.lyj.api.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase =
        Room.databaseBuilder(
            context, LocalDatabase::class.java,"pinstagram.db"
        ).build()
}