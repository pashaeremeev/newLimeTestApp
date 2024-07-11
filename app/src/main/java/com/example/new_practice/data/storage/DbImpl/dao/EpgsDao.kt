package com.example.new_practice.data.storage.DbImpl.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.new_practice.data.storage.entities.Epg
import com.example.new_practice.domain.models.EpgModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EpgsDao {
    @Query("SELECT * FROM epgs")
    fun getEpgs(): LiveData<List<Epg>>

    @Query("SELECT * FROM epgs")
    fun getEpgsNow(): List<Epg>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEpgs(epgs: List<Epg>)

    @Query("SELECT * FROM epgs WHERE channelId = :channelId")
    fun getEpgByChannelId(channelId: Int): LiveData<List<Epg>>

    @Query("SELECT * FROM epgs WHERE channelId = :channelId")
    fun getEpgByChannelIdNow(channelId: Int): List<Epg>

    @Query("SELECT * FROM epgs")
    fun getEpgsFlow(): Flow<List<Epg>>

}