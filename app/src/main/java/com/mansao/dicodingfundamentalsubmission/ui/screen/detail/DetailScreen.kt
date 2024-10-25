package com.mansao.dicodingfundamentalsubmission.ui.screen.detail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.EventDto
import com.mansao.dicodingfundamentalsubmission.ui.common.UiState

@Composable
fun DetailScreen(
    eventId: Int,
    navigateBack: () -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        detailViewModel.getDetail(eventId)
    }
    val context = LocalContext.current
    Scaffold(
        topBar = { DetailTopBar(navigateBack = navigateBack) }

    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            DetailUiState(
                detailViewModel = detailViewModel,
                context = context
            )
        }
    }
}


@Composable
fun DetailUiState(
    detailViewModel: DetailViewModel,
    context: Context,
) {
    detailViewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {}

            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Composable
fun DetailContent(modifier: Modifier = Modifier, event: EventDto) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(navigateBack: () -> Unit) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBackIosNew, contentDescription = null)
            }
        })
}