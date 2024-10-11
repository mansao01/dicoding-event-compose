package com.mansao.dicodingfundamentalsubmission.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.ui.graphics.vector.ImageVector

sealed class UiState<out T : Any?> {
    data object Standby : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
}

sealed interface SettingThemeUiState {
    data class SettingThemeUiState(
        val isDarkMode: Boolean = false,
        val title: String = if (isDarkMode) "Dark Mode" else "Light Mode",
        val icon: ImageVector =
            if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode
    )
}
sealed interface SettingNotificationUiState {
    data class SettingNotificationUiState(
        val isPush: Boolean = false,
        val title: String = if (isPush) "Push notification off" else "Push notification on",
        val icon: ImageVector =
            if (isPush) Icons.Default.Notifications else Icons.Default.NotificationsOff,

    )
}