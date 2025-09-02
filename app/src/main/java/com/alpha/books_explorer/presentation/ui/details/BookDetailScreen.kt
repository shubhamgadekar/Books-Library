package com.alpha.books_explorer.presentation.ui.details

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.alpha.books_explorer.R
import com.alpha.books_explorer.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookDetailViewModel = hiltViewModel(),
) {
    val book by viewModel.bookState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(bookId) {
        if (book.book?.id != bookId) {
            viewModel.fetchBookById(bookId)
        }
        viewModel.checkWishlistItem(book.book)
        viewModel.checkReadinglistItem(book.book)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        book.book?.volumeInfo?.title ?: "Book Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    book.book.let { currentBook ->
                        IconButton(onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out this book: ${currentBook?.volumeInfo?.title} by ${
//                                        currentBook?.volumeInfo?.authors
                                        currentBook?.volumeInfo?.authors?.joinToString(", ")
                                    }\n\n${currentBook?.volumeInfo?.description?.take(100)}...",
                                )
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Book Via")
                            context.startActivity(shareIntent)
                        }) {
                            Icon(Icons.Filled.Share, contentDescription = "Share Book")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { paddingValues ->
        when {
            book.isLoading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            book.error != null -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Book not found or error loading.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            else -> {
                BookDetailsContent(
                    book = book.book,
                    modifier = Modifier.padding(paddingValues),
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
fun BookDetailsContent(
    book: Book?,
    modifier: Modifier = Modifier,
    viewModel: BookDetailViewModel,
) {
    if (book == null) return

    val isItemInWishlist = viewModel.checkWishlistItem.collectAsState().value
    val isItemInReadinglist = viewModel.checkReadinglistItem.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxWidth() // Changed from fillMaxSize to allow padding from Scaffold
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Top section with Thumbnail and core info
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top, // Align items to the top of the row
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Space between image and text column
        ) {
            // Book Thumbnail
            Image(
                painter = if (book.volumeInfo?.imageLinks?.thumbnail != null) {
                    rememberAsyncImagePainter(
                        model = book.volumeInfo?.imageLinks?.thumbnail,
                        // It's good practice to provide placeholders and error drawables for network images
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        error = painterResource(id = R.drawable.ic_launcher_background),
                    )
                } else {
                    // Fallback for when thumbnail is null
                    painterResource(id = R.drawable.ic_launcher_background)
                },
                contentDescription = book.volumeInfo?.title,
                modifier = Modifier
                    .size(width = 130.dp, height = 190.dp) // Slightly larger thumbnail
                    .clip(RoundedCornerShape(8.dp)) // Softer corners
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                // Placeholder bg
                contentScale = ContentScale.Crop, // Crop to fill bounds
            )

            // Title, Author, and other short info
            Column(
                modifier = Modifier.weight(1f), // Takes remaining space
            ) {
                Text(
                    text = book.volumeInfo?.title ?: "Dummy Title",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "by ${book.volumeInfo?.authors?.joinToString(", ")}",
//                    text = "by ${book.volumeInfo?.authors}",
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp)) // Increased spacing

        // Description Section
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        Text(
            text = book.volumeInfo?.description ?: "Dummy Description",
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp, // Improve readability for long text
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Example: Action Buttons ---
        // You can add buttons for actions like "Add to Wishlist", "Read Now", etc.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End), // Align to end
        ) {
            FavoriteTextToggleButton(
                isFavorite = isItemInWishlist,
                onFavoriteChanged = { isNowFavorite ->
                    if (isNowFavorite) {
                        println("Item ADDED to wishlist with text button")
                        viewModel.addToWishlist(book)
                    } else {
                        println("Item REMOVED from wishlist with text button")
                        viewModel.removeFromWishList(book)
                    }
                },
            )

            ReadListToggleButton(
                isFavorite = isItemInReadinglist,
                onFavoriteChanged = { isNowFavorite ->
                    if (isNowFavorite) {
                        println("Item ADDED to wishlist with text button")
                        viewModel.addToReadinglist(book)
                    } else {
                        println("Item REMOVED from wishlist with text button")
                        viewModel.removeFromReadingList(book)
                    }
                },
            )
        }
        Spacer(modifier = Modifier.height(16.dp)) // Bottom padding
    }
}

@Composable
fun FavoriteTextToggleButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteChanged: (Boolean) -> Unit,
    iconTint: Color = Color.Red, // Tint for the filled heart
    contentColor: Color = MaterialTheme.colorScheme.primary, // Default content color for button
) {
    TextButton(
        onClick = { onFavoriteChanged(!isFavorite) }, // Toggle the state on click
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center, // Or Start, if you prefer
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from Wishlist" else "Add to Wishlist",
                tint = if (isFavorite) iconTint else contentColor, // Apply specific tint when favorited
                modifier = Modifier.size(ButtonDefaults.IconSize), // Standard button icon size
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing)) // Standard spacing
            Text(
                text = if (isFavorite) "In Wishlist" else "Add to Wishlist",
            )
        }
    }
}

@Composable
fun ReadListToggleButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteChanged: (Boolean) -> Unit,
    iconTint: Color = Color.Green, // Tint for the filled heart
    contentColor: Color = MaterialTheme.colorScheme.primary, // Default content color for button
) {
    TextButton(
        onClick = { onFavoriteChanged(!isFavorite) }, // Toggle the state on click
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center, // Or Start, if you prefer
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Done else Icons.Outlined.Done,
                contentDescription = if (isFavorite) "Remove from Wishlist" else "Add to Wishlist",
                tint = if (isFavorite) iconTint else contentColor, // Apply specific tint when favorited
                modifier = Modifier.size(ButtonDefaults.IconSize), // Standard button icon size
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing)) // Standard spacing
            Text(
                text = if (isFavorite) "Read List" else "Read List",
            )
        }
    }
}
