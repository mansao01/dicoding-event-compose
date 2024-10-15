package com.mansao.dicodingfundamentalsubmission.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.dicodingfundamentalsubmission.data.DicodingEventRepositoryImpl
import com.mansao.dicodingfundamentalsubmission.ui.common.SettingNotificationUiState
import com.mansao.dicodingfundamentalsubmission.ui.common.SettingThemeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val dicodingEventRepositoryImpl: DicodingEventRepositoryImpl) :
    ViewModel() {

    val isPushNotification =
        dicodingEventRepositoryImpl.getPushNotification().map { isPushNotification ->
            SettingNotificationUiState.SettingNotificationUiState(isPushNotification)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SettingNotificationUiState.SettingNotificationUiState()
        )

    val isDarkMode =
        dicodingEventRepositoryImpl.getIsDarkMode().map { isDarkMode ->
            SettingThemeUiState.SettingThemeUiState(isDarkMode)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SettingThemeUiState.SettingThemeUiState()
        )


    fun savePushNotificationState(newValue: Boolean) = viewModelScope.launch {
        dicodingEventRepositoryImpl.savePushNotification(newValue)
    }

    fun selectedTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            dicodingEventRepositoryImpl.saveIsDarkMode(isDarkMode)
        }
    }

}