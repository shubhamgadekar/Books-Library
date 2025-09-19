package com.alpha.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readingList")
internal data class ReadingListEntity(
    @PrimaryKey
    val id: String,
    val title: String?,
    val subtitle: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val thumbnail: String?,
)
