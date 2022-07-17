package com.example.navigtion_component.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.navigtion_component.model.News

@Dao
interface NewsDAO {
    @Insert
    fun onInsert(new: News)

    @Query("select * from news")
    fun getList(): List<News>

    @Query("select count(id) from news")
    fun count(): Int

    @Query("select * from news where id = :id limit 1")
    fun getListId(id: String): List<News>
}