package com.mansao.dicodingfundamentalsubmission.ui.screen.settings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onDarkModeChanged: (Boolean) -> Unit,
    onNotificationChanged: (Boolean) -> Unit
) {


    val isPushNotification by settingsViewModel.isPushNotification.collectAsState()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

    Log.d(" state", "notification :$isPushNotification.toString()")
    Log.d(" state", "theme :$isDarkMode.toString()")
    Column {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Push Notification",
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 20.sp
            )
            Switch(
                checked = isPushNotification.isPush,
                onCheckedChange = { isChecked ->
                    settingsViewModel.savePushNotificationState(isChecked)
                    onNotificationChanged(isChecked)
                },
                thumbContent = {
                    Icon(
                        imageVector = isPushNotification.icon,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                },
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = isDarkMode.title, modifier = Modifier.padding(top = 4.dp), fontSize = 20.sp)
            Switch(
                checked = isDarkMode.isDarkMode,
                onCheckedChange = { isChecked ->
                    settingsViewModel.selectedTheme(isChecked)
                    onDarkModeChanged(isChecked)
                },
                thumbContent = {
                    Icon(
                        imageVector = isDarkMode.icon,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                },
            )
        }
    }
}

