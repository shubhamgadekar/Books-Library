package com.alpha.books_explorer

import androidx.lifecycle.ViewModel
import com.alpha.books_explorer.domain.model.Book
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

class MainAppDoorViewModel @Inject constructor() : ViewModel() {

    fun processBookByIdResponse(payload: JsonObject?) {
        val book: Book = Gson().fromJson(payload, Book::class.java)
        println("Shubham: Successfully converted payload to Book: ${book}")
    }
}
