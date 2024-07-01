package com.example.new_practice.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.new_practice.storage.entities.Channel
import com.example.new_practice.storage.dao.ChannelsDao
import com.example.new_practice.storage.dao.EpgsDao
import com.example.new_practice.storage.entities.Epg

@Database(
    entities = [
        Channel::class,
        Epg::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun channelsDao(): ChannelsDao

    abstract fun epgsDao(): EpgsDao

}