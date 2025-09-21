package com.alpha.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favBooks")
internal data class BookEntity(
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
