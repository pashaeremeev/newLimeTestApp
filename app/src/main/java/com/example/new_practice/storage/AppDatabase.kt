package com.example.new_practice.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.new_practice.storage.entities.ChannelData

@Database(
    entities = [
        ChannelData::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {


}