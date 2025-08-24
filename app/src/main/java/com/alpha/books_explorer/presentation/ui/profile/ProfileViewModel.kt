package com.alpha.books_explorer.presentation.ui.profile

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ProfileViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow(UserProfile()) // Default/empty profile
    val userProfile: StateFlow<UserProfile> = _userProfile

    // In a real app, you would fetch this data, e.g., from a repository
    fun loadUserProfile() {
        // Simulate loading
        // _userProfile.value = UserProfile(firstName = "Jane", lastName = "Smith", email = "jane.s@example.com")
    }

    // If you want editable fields:
    fun updateFirstName(name: String) {
        _userProfile.value = _userProfile.value.copy(firstName = name)
    }
    fun updateLastName(name: String) {
        _userProfile.value = _userProfile.value.copy(lastName = name)
    }
}

data class UserProfile(
    val firstName: String = "John",
    val lastName: String = "Doe",
    val email: String = "john.doe@example.com"
)