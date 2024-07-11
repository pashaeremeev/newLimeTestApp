package com.example.new_practice.data.storage.DbImpl.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.new_practice.data.storage.entities.Channel
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelsDao {
    @Query("SELECT * FROM channels")
    fun getChannels(): LiveData<List<Channel>>

    @Query("SELECT * FROM channels WHERE isFavorite")
    fun getFavChannels(): LiveData<List<Channel>>

    @Query("SELECT * FROM channels")
    fun getChannelsNow(): List<Channel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveChannels(channels: List<Channel>)

    @Query("SELECT * FROM channels WHERE id = :channelId")
    fun getChannelById(channelId: Int): LiveData<List<Channel>>

    @Query("UPDATE channels SET isFavorite = not(isFavorite) WHERE id = :channelId")
    fun changeFav(channelId: Int)

    @Query("SELECT * FROM channels WHERE name like '%'||:text||'%'")
    fun searchChannels(text: String): Flow<List<Channel>>

    @Query("SELECT * FROM channels")
    fun getChannelsFlow(): Flow<List<Channel>>
}