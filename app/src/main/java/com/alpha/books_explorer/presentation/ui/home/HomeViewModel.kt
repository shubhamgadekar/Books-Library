package com.alpha.books_explorer.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.domain.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class HomeViewModel
@Inject
constructor(
    private val appDoor: MainAppDoor
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadBooks()
        appDoor.getReadingList()
    }

    internal fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            appDoor.readingList.collect {
                if (it.payload?.has("error") == true && it.payload?.get("error")?.asString?.contains("empty") == true) {
                    _uiState.value = HomeUiState(isLoading = false)
                } else {
                    val type = object : TypeToken<List<Book>>() {}.type
                    val books: List<Book> = Gson().fromJson(it.payload?.getAsJsonArray("books"), type)
                    _uiState.value = HomeUiState(books = books)
                }
            }
        }
    }
}
