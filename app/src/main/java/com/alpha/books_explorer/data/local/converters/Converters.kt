package com.alpha.books_explorer.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromAuthorsList(authors: List<String>?): String? {
        return Gson().toJson(authors)
    }

    @TypeConverter
    fun toAuthorsList(authorsString: String?): List<String>? {
        if (authorsString.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(authorsString, listType)
    }
}
