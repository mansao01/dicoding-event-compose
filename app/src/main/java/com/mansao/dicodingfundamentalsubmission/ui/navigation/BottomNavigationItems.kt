package com.mansao.dicodingfundamentalsubmission.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
data class BottomNavigationItems<T : Any>(
    val title: String,
    val icon: ImageVector,
    val screen: T,
    val contentDescription: String
)
