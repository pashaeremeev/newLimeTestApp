package com.example.new_practice.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.new_practice.storage.entities.ChannelData

@Dao
interface ChannelsDao {
    @Query("SELECT * FROM channels")
    fun getChannels(): LiveData<List<ChannelData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveChannels(channels: List<ChannelData>)
}