package com.alpha.books_explorer.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alpha.books_explorer.presentation.ui.details.BookDetailScreen
import com.alpha.books_explorer.presentation.ui.details.BookDetailViewModel
import com.alpha.books_explorer.presentation.ui.home.HomeScreen
import com.alpha.books_explorer.presentation.ui.home.HomeViewModel
import com.alpha.books_explorer.presentation.ui.profile.ProfileScreen
import com.alpha.books_explorer.presentation.ui.search.SearchScreen
import com.alpha.books_explorer.presentation.ui.search.SearchScreenViewModel
import com.alpha.books_explorer.presentation.ui.wishList.WishlistScreen
import com.alpha.books_explorer.presentation.ui.wishList.WishlistViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
@Composable
fun MyAppNavHost(innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController, innerPadding = innerPadding)
        }
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("wishlistScreen") {
            WishlistScreen(navController = navController)
        }
        composable("bookDetailsScreen/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDetailScreen(
                navController = navController,
                bookId = bookId
            )
        }
    }
}
