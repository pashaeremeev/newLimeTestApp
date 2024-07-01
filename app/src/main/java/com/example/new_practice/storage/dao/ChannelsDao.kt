package com.example.new_practice.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.new_practice.storage.entities.Channel

@Dao
interface ChannelsDao {
    @Query("SELECT * FROM channels")
    fun getChannels(): LiveData<List<Channel>>

    @Query("SELECT * FROM channels")
    fun getChannelsNow(): List<Channel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveChannels(channels: List<Channel>)

    @Query("SELECT * FROM channels WHERE id = :channelId")
    fun getChannelById(channelId: Int): LiveData<List<Channel>>

    @Query("UPDATE channels SET isFavorite = not(isFavorite) WHERE id = :channelId")
    fun changeFav(channelId: Int)
}