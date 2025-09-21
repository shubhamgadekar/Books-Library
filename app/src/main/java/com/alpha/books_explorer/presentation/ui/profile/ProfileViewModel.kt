package com.alpha.books_explorer.presentation.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class ProfileViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    fun updateFirstName(name: String) {
        _userProfile.value = _userProfile.value.copy(firstName = name)
    }

    fun updateLastName(name: String) {
        _userProfile.value = _userProfile.value.copy(lastName = name)
    }
}

internal data class UserProfile(
    val firstName: String = "John",
    val lastName: String = "Doe",
    val email: String = "john.doe@example.com",
)
