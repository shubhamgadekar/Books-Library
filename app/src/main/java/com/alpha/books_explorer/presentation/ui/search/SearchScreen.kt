package com.alpha.books_explorer.presentation.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.presentation.ui.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchScreenViewModel = hiltViewModel(),
) {
    var searchQuery = viewModel.searchText.collectAsState("").value
    var searchUiState = viewModel.searchBookList.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Search Books") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.updateSearchText(it)
                },
                label = { Text("Enter search query") },
                modifier = Modifier.fillMaxWidth(),
            )
            // Add your search results list or other UI elements here
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                when {
                    searchUiState.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    searchUiState.error != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Error: ${searchUiState.error}")
                        }
                    }

                    else -> {
                        searchUiState.books?.let { pagingFlow ->
                            val books = pagingFlow.collectAsLazyPagingItems()
                            SearchBookList(
                                books = books,
                                navController,
                                emptyMessage = "Search for any keyword to get book list",
                            )
                        } ?: ShowEmptyScreen("Search for any keyword to get book list")
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBookList(
    books: LazyPagingItems<Book>,
    navController: NavController,
    emptyMessage: String = "There is something with list. List is empty now!",
) {
    if (books.itemCount > 0) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(8.dp),
        ) {
            items(books.itemCount) { book ->
                if (books[book] != null) {
                    BookCard(book = books[book]!!, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    } else {
        ShowEmptyScreen(emptyMessage)
    }
}

@Composable
fun ShowEmptyScreen(emptyMessage: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(emptyMessage)
    }
}
