package com.mansao.dicodingfundamentalsubmission.ui.screen.settings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mansao.dicodingfundamentalsubmission.worker.MyWorker
import java.util.concurrent.TimeUnit

private lateinit var workManager: WorkManager
private lateinit var periodicWorkRequest: PeriodicWorkRequest

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    onDarkModeChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    workManager = WorkManager.getInstance(context)

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
        .setConstraints(constraints)
        .build()

    val isPushNotification by settingsViewModel.isPushNotification.collectAsState()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

    // Update the notification state based on the current setting
    updateNotificationState(isPushNotification.isPush, periodicWorkRequest, lifecycleOwner)

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = isPushNotification.title,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 20.sp
            )
            IconButton(onClick = {
                // Toggle the push notification state
                val newState = !isPushNotification.isPush
                settingsViewModel.savePushNotificationState(newState)

                // Update notification state to start or stop periodic task
                updateNotificationState(newState, periodicWorkRequest, lifecycleOwner)
            }) {
                Icon(
                    imageVector = if (isPushNotification.isPush) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                    contentDescription = null
                )
            }
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

private fun startPeriodicTask(
    periodicWorkRequest: PeriodicWorkRequest,
    lifecycleOwner: LifecycleOwner

) {

    workManager.enqueue(periodicWorkRequest)
    workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
        .observe(lifecycleOwner) { workInfo ->
            val statusResult = workInfo.state.name
            Log.d("start Work", "Status : $statusResult")
        }

}

private fun cancelWorkPeriodicTask(
    periodicWorkRequest: PeriodicWorkRequest,
    lifecycleOwner: LifecycleOwner
) {
    workManager.cancelWorkById(periodicWorkRequest.id)
    workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id)
        .observe(lifecycleOwner) { workInfo ->
            val statusResult = workInfo.state.name
            Log.d("Cancel Work", "Status : $statusResult")
        }
}

fun updateNotificationState(
    isChecked: Boolean,
    periodicWorkRequest: PeriodicWorkRequest,
    lifecycleOwner: LifecycleOwner
) {
    Log.d("is checked:", isChecked.toString())
    if (isChecked) startPeriodicTask(
        periodicWorkRequest,
        lifecycleOwner
    ) else cancelWorkPeriodicTask(
        periodicWorkRequest, lifecycleOwner
    )

}
