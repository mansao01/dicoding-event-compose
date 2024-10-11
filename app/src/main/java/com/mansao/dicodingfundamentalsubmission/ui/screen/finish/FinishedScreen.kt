package com.mansao.dicodingfundamentalsubmission.ui.screen.finish

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.EventDto
import com.mansao.dicodingfundamentalsubmission.ui.common.UiState
import com.mansao.dicodingfundamentalsubmission.ui.components.EventColumnItem

@Composable
fun FinishedScreen(
    finishedViewModel: FinishedViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit,
    ) {
    val context = LocalContext.current
    FinishedEventUiState(
        finishedViewModel = finishedViewModel,
        context = context,
        navigateToDetail = navigateToDetail
    )
}

@Composable
fun FinishedEventUiState(
    finishedViewModel: FinishedViewModel,
    context: Context,
    navigateToDetail: (Int) -> Unit,
) {
    finishedViewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> FinishedEventList(
                events = uiState.data.listEvents,
                context = context,
                navigateToDetail = navigateToDetail,
            )

            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Composable
fun FinishedEventList(
    events: List<EventDto>,
    context: Context,
    navigateToDetail: (Int) -> Unit,
) {
    LazyColumn {
        items(events, key = { it.id }) { event ->
            EventColumnItem(
                context = context,
                event = event,
                modifier = Modifier.clickable { navigateToDetail(event.id) })
        }
    }
}