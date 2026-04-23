package dev.kbwallet.app.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileState(
    val displayName: String = "Quartz",
    val email: String = "quartz@email.com",
    val avatarInitial: String = "Q",

    // Notification settings
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = false,
    val priceAlerts: Boolean = true,
    val tradeConfirmations: Boolean = true,
    val newsUpdates: Boolean = false,

    // Security settings
    val biometricAuth: Boolean = false,
    val twoFactorAuth: Boolean = false
)

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun updateProfile(name: String, email: String) {
        _state.update {
            it.copy(
                displayName = name.trim(),
                email = email.trim(),
                avatarInitial = name.trim().firstOrNull()?.uppercase() ?: "?"
            )
        }
    }

    // --- Notification toggles ---

    fun togglePushNotifications() {
        _state.update { it.copy(pushNotifications = !it.pushNotifications) }
    }

    fun toggleEmailNotifications() {
        _state.update { it.copy(emailNotifications = !it.emailNotifications) }
    }

    fun togglePriceAlerts() {
        _state.update { it.copy(priceAlerts = !it.priceAlerts) }
    }

    fun toggleTradeConfirmations() {
        _state.update { it.copy(tradeConfirmations = !it.tradeConfirmations) }
    }

    fun toggleNewsUpdates() {
        _state.update { it.copy(newsUpdates = !it.newsUpdates) }
    }

    // --- Security toggles ---

    fun toggleBiometricAuth() {
        _state.update { it.copy(biometricAuth = !it.biometricAuth) }
    }

    fun toggleTwoFactorAuth() {
        _state.update { it.copy(twoFactorAuth = !it.twoFactorAuth) }
    }
}
