package com.alpha.books_explorer.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alpha.books_explorer.presentation.ui.details.BookDetailScreen
import com.alpha.books_explorer.presentation.ui.home.HomeScreen
import com.alpha.books_explorer.presentation.ui.profile.ProfileScreen
import com.alpha.books_explorer.presentation.ui.search.SearchScreen
import com.alpha.books_explorer.presentation.ui.wishList.WishlistScreen
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun MyAppNavHost(innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
        composable("home") {
            HomeScreen(navController = navController)
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
                bookId = bookId,
            )
        }
    }
}
