package com.alpha.books_explorer.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.alpha.books_explorer.domain.model.Book

@Composable
fun BookList(
    books: List<Book>,
    navController: NavController,
    emptyMessage: String = "There is something with list. List is empty now!",
) {
    if (books.isNotEmpty()) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(8.dp),
        ) {
            items(books.size) { book ->
                BookCard(book = books[book], navController)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(emptyMessage)
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    navController: NavController,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    navController.navigate("bookDetailsScreen/${book.id}")
                },
    ) {
        Image(
            painter = rememberAsyncImagePainter(book.volumeInfo?.imageLinks?.thumbnail),
            contentDescription = book.volumeInfo?.title,
            modifier = Modifier.size(80.dp),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = book.volumeInfo?.title ?: "Dummy Title",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = book.volumeInfo?.authors?.joinToString(", ") ?: "",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
