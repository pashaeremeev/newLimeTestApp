package com.example.new_practice.storage

import android.content.Context
import androidx.room.Room

class RoomInstance {

    companion object {
        private var dataBase: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (dataBase == null) {
                dataBase = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "database"
                )
                .build()
            }
            return dataBase!!
        }
    }
}