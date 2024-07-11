package com.example.new_practice.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.new_practice.domain.repos.EpgRepository
import com.example.new_practice.data.storage.DbImpl.AppDatabase
import com.example.new_practice.data.storage.DbImpl.RoomInstance
import com.example.new_practice.data.storage.entities.Channel
import com.example.new_practice.data.storage.entities.Epg
import com.example.new_practice.domain.models.EpgModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EpgRepositoryImpl private constructor(context: Context): EpgRepository {

    private val preferencesRepo: PreferencesRepo
    private val appDatabase: AppDatabase

    init {
        preferencesRepo = PreferencesRepo(context)
        appDatabase = RoomInstance.getInstance(context)
    }

    override fun saveEpgs(epgs: ArrayList<EpgModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            val newEpgs = mutableListOf<Epg>()
            for (epg in epgs) {
                newEpgs.add(fromEpgModelToEpg(epg))
            }
            appDatabase.epgsDao().saveEpgs(newEpgs)
        }
    }

    val epgsFlow: Flow<List<EpgModel>> = appDatabase.epgsDao().getEpgsFlow().map {
        it.map { epg ->
            epg.toEpgModel()
        }
    }

    override fun getEpgs(): Flow<List<EpgModel>> {
        return appDatabase.epgsDao().getEpgsFlow().map {
            it.map { epg ->
                epg.toEpgModel()
            }
        }
    }

    val epgs: LiveData<List<Epg>>
        get() {
            return appDatabase.epgsDao().getEpgs()
        }

    fun getByChannelId(id: Int): LiveData<EpgModel?> {
        return appDatabase.epgsDao().getEpgByChannelId(id).map {
            return@map it.firstOrNull()?.toEpgModel()
        }
    }

    fun getByChannelIdNow(id: Int): EpgModel? {
        return appDatabase.epgsDao().getEpgByChannelIdNow(id).firstOrNull()?.toEpgModel()
    }

    fun fromEpgModelToEpg(epgModel: EpgModel): Epg {
        return Epg(
            epgModel.id,
            epgModel.channelId,
            epgModel.timeStart,
            epgModel.timeStop,
            epgModel.title
        )
    }

    companion object {
        //private const val KEY_EPG = "epg"
        private var epgRepo: EpgRepositoryImpl? = null
        fun getInstance(context: Context?): EpgRepositoryImpl {
            if (epgRepo == null) {
                epgRepo = EpgRepositoryImpl(context!!)
            }
            return epgRepo!!
        }
    }
}