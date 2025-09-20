package com.alpha.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal class Converters {
    @TypeConverter
    internal fun fromAuthorsList(authors: List<String>?): String? {
        return Gson().toJson(authors)
    }

    @TypeConverter
    internal fun toAuthorsList(authorsString: String?): List<String>? {
        if (authorsString.isNullOrEmpty()) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(authorsString, listType)
    }
}
