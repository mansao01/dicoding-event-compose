package com.mansao.dicodingfundamentalsubmission

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.dicodingfundamentalsubmission.ui.DicodingEventApp
import com.mansao.dicodingfundamentalsubmission.ui.screen.settings.SettingsViewModel
import com.mansao.dicodingfundamentalsubmission.ui.theme.DicodingFundamentalSubmissionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
        setContent {
            val settingVIewModel: SettingsViewModel = hiltViewModel()
            var isDarkMode = settingVIewModel.isDarkMode.collectAsState().value.isDarkMode
            installSplashScreen()
            DicodingFundamentalSubmissionTheme(isDarkMode) {
                DicodingEventApp(onDarkModeChanged = {newDarkMode ->
                    isDarkMode = newDarkMode
                })
            }
        }
    }
}

