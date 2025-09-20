package com.alpha.books_explorer.presentation.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.presentation.ui.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel(),
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
            modifier = Modifier
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
                        searchUiState.books?.let { books ->
                            if (books.isEmpty()) {
                                if (searchUiState.querySearched && !searchUiState.isLoading) {
                                    ShowEmptyScreen("No books found for your query.")
                                } else if (!searchUiState.querySearched && !searchUiState.isLoading) {
                                    ShowEmptyScreen("Search for any keyword to get book list.")
                                }
                            } else {
                                SearchBookList(
                                    books = books,
                                    navController = navController,
                                    isLoadingNextPage = searchUiState.isLoadingNextPage,
                                    errorNextPage = if (searchUiState.isLoadingNextPage || searchUiState.isLoading) null else searchUiState.error,
                                    loadNextPage = {
                                        viewModel.loadNextPage()
                                    }
                                )
                            }
                        } ?: ShowEmptyScreen("Search for any keyword to get book list")
                    }
                }
            }
        }
    }
}

@Composable
internal fun SearchBookList(
    books: List<Book>,
    navController: NavController,
    isLoadingNextPage: Boolean,
    errorNextPage: String?,
    loadNextPage: () -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp), // Add vertical padding for the list itself
        contentPadding = PaddingValues(bottom = if (isLoadingNextPage || errorNextPage != null) 56.dp else 0.dp)
    ) {
        items(count = books.size, key = { index -> books[index].id }) { innerIndex ->
            BookCard(book = books[innerIndex], navController = navController)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isLoadingNextPage || errorNextPage != null) {
            item {
                PaginationFooter(isLoadingNextPage, errorNextPage, onRetry = loadNextPage)
            }
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)

            totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 5 && !isLoadingNextPage && errorNextPage == null
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            loadNextPage()
        }
    }
}

@Composable
internal fun PaginationFooter(isLoading: Boolean, error: String?, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
            }
            error != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error loading more: $error", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
internal fun ShowEmptyScreen(emptyMessage: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(emptyMessage)
    }
}
