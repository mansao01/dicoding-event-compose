package com.mansao.dicodingfundamentalsubmission

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mansao.dicodingfundamentalsubmission.ui.DicodingEventApp
import com.mansao.dicodingfundamentalsubmission.ui.screen.settings.SettingsViewModel
import com.mansao.dicodingfundamentalsubmission.ui.theme.DicodingFundamentalSubmissionTheme
import com.mansao.dicodingfundamentalsubmission.worker.MyWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        workManager = WorkManager.getInstance(this)


        setContent {
            val settingVIewModel: SettingsViewModel = hiltViewModel()
            var isDarkMode = settingVIewModel.isDarkMode.collectAsState().value.isDarkMode
            val isPushNotification =
                settingVIewModel.isPushNotification.collectAsState().value.isPush
            Log.d("state", "notification :$isPushNotification")
            installSplashScreen()
            DicodingFundamentalSubmissionTheme(isDarkMode) {
                DicodingEventApp(
                    onDarkModeChanged = { newDarkMode ->
                        isDarkMode = newDarkMode
                    },
                    onNotificationChanged = { newNotificationState ->
                        updateNotificationState(newNotificationState, this)
                    }
                )
            }
        }
    }

    private fun startPeriodicTask(
        lifecycleOwner: LifecycleOwner

    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

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

    private fun updateNotificationState(
        isChecked: Boolean,
        lifecycleOwner: LifecycleOwner
    ) {
        Log.d("is checked:", isChecked.toString())
        if (isChecked) startPeriodicTask(lifecycleOwner)
        else cancelWorkPeriodicTask(
            periodicWorkRequest, lifecycleOwner
        )

    }

}


