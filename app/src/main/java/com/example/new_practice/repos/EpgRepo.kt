package com.example.new_practice.repos

import android.content.Context
import com.example.new_practice.Epg
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.Exception

class EpgRepo private constructor(context: Context) {

    private val preferencesRepo: PreferencesRepo

    fun saveEpgs(epgs: ArrayList<Epg>) {
        val channelsJson: String = Gson().toJson(epgs)
        preferencesRepo.save(channelsJson, KEY_EPG)
    }

    val epgs: ArrayList<Epg>
        get() {
            val listJsonString: String? = preferencesRepo.get(KEY_EPG)
            val type = object : TypeToken<ArrayList<Epg?>?>() {}.type
            val epgs: ArrayList<Epg> = try {
                Gson().fromJson(listJsonString, type)
            } catch (e: Exception) {
                arrayListOf()
            }
            return epgs ?: ArrayList()
        }

    fun getById(id: Int): Epg? {
        val epgs: ArrayList<Epg> = epgs
        for (i in epgs.indices) {
            val epg: Epg = epgs[i]
            if (epg.channelId === id) {
                return epg
            }
        }
        return null
    }

    init {
        preferencesRepo = PreferencesRepo(context)
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