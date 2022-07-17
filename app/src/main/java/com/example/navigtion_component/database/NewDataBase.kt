package com.example.navigtion_component.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.navigtion_component.model.News

@Database(entities = [News::class], version = 1)
abstract class NewDataBase : RoomDatabase() {
    abstract fun newDAO(): NewsDAO

    companion object {

        private var INSTANCE: NewDataBase? = null

        fun getDatabase(context: Context): NewDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewDataBase::class.java, "database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}