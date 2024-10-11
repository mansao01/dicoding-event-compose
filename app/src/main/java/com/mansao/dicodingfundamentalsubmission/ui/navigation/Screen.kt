package com.mansao.dicodingfundamentalsubmission.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Home

    @Serializable
    data class Detail(val id: Int)

    @Serializable
    object Upcoming

    @Serializable
    object Finished

    @Serializable
    object Settings
}