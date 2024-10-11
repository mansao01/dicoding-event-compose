package com.mansao.dicodingfundamentalsubmission.ui.screen.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailScreen(eventId: Int, detailViewModel: DetailViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        detailViewModel.getDetail(eventId)
    }
    Text(text = eventId.toString())
}