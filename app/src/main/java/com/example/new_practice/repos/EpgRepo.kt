package com.example.new_practice.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.new_practice.storage.AppDatabase
import com.example.new_practice.storage.RoomInstance
import com.example.new_practice.storage.entities.Epg
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class EpgRepo private constructor(context: Context) {

    private val preferencesRepo: PreferencesRepo
    private val appDatabase: AppDatabase

    init {
        preferencesRepo = PreferencesRepo(context)
        appDatabase = RoomInstance.getInstance(context)
    }

    fun saveEpgs(epgs: ArrayList<Epg>) {
        /*val channelsJson: String = Gson().toJson(epgs)
        preferencesRepo.save(channelsJson, KEY_EPG)*/
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.epgsDao().saveEpgs(epgs)
        }
    }

    val epgs: LiveData<List<Epg>>
        get() {
            /*val listJsonString: String? = preferencesRepo[KEY_EPG]
            val type = object : TypeToken<ArrayList<Epg?>?>(){}.type
            val epgs: ArrayList<Epg> = try {
                Gson().fromJson(listJsonString, type)
            } catch (e: Exception) {
                arrayListOf()
            }
            return MutableLiveData(epgs ?: ArrayList())*/
            return appDatabase.epgsDao().getEpgs()
        }

    fun getByChannelId(id: Int): LiveData<Epg?> {
        /*return epgRepo!!.epgs.map {
            return@map it.firstOrNull { epg -> epg.channelId == id }
        }*/
        return appDatabase.epgsDao().getEpgByChannelId(id).map { return@map it.firstOrNull() }
    }

    companion object {
        private const val KEY_EPG = "epg"
        private var epgRepo: EpgRepo? = null
        fun getInstance(context: Context?): EpgRepo {
            if (epgRepo == null) {
                epgRepo = EpgRepo(context!!)
            }
            return epgRepo!!
        }
    }
}