package com.alpha.books_explorer.presenatation.ui.profile

import app.cash.turbine.test
import com.alpha.books_explorer.presentation.ui.details.BookDetailsUiState
import com.alpha.books_explorer.presentation.ui.profile.ProfileViewModel
import com.alpha.books_explorer.presentation.ui.profile.UserProfile
import org.junit.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class ProfileViewModelTest {

    lateinit var profileViewModel: ProfileViewModel

    @Test
    fun testProfileViewModel() = runTest {
        profileViewModel = ProfileViewModel()

        profileViewModel.loadUserProfile()

        profileViewModel.updateFirstName("Shubham")

        profileViewModel.userProfile.test {
            assertEquals(UserProfile(firstName = "Shubham"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        profileViewModel.updateLastName("Gadekar")

        profileViewModel.userProfile.test {
            assertEquals(UserProfile(firstName = "Shubham", lastName = "Gadekar"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
