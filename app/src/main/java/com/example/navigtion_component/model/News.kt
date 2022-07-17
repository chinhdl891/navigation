package com.example.navigtion_component.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class News(
    @ColumnInfo
    var content: String? = "",
    @ColumnInfo
    var date: String? = "",
    @PrimaryKey
    var id: String = "",
    @ColumnInfo
    var image: String? = "" ,
    @ColumnInfo
    var title: String? = "",
    @ColumnInfo
    var upLoadBy: String? = "",
    @ColumnInfo
    var key: String? = "",
    @ColumnInfo
    var keyUpload: String? = ""
)