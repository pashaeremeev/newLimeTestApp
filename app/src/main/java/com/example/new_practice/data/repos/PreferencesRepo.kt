package com.example.new_practice.data.repos

import android.content.Context
import android.content.SharedPreferences

class PreferencesRepo(context: Context?) {
    private val preferences: SharedPreferences

    init {
        preferences = context!!.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun save(listJsonString: String?, key: String?) {
        val editor = preferences.edit()
        editor.putString(key, listJsonString)
        editor.commit()
    }

    operator fun get(key: String?): String? {
        return preferences.getString(key, "")
    }

    companion object {
        const val APP_PREFERENCES = "APP_PREFERENCES"
    }
}