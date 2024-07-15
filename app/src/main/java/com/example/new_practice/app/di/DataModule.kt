package com.example.new_practice.app.di

import android.content.Context
import androidx.room.Room
import com.example.new_practice.data.repos.ChannelRepositoryImpl
import com.example.new_practice.data.repos.EpgRepositoryImpl
import com.example.new_practice.data.storage.implDb.AppDatabase
import com.example.new_practice.domain.repos.ChannelRepository
import com.example.new_practice.domain.repos.EpgRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEpgRepository(appDatabase: AppDatabase): EpgRepository {
        return EpgRepositoryImpl(appDatabase)
    }

    @Provides
    @Singleton
    fun provideChannelRepository(appDatabase: AppDatabase, epgRepository: EpgRepository): ChannelRepository {
        return ChannelRepositoryImpl(appDatabase, epgRepository)
    }
}