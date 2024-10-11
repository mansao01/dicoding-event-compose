package com.mansao.dicodingfundamentalsubmission.ui.screen.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.EventDto
import com.mansao.dicodingfundamentalsubmission.ui.common.UiState
import com.mansao.dicodingfundamentalsubmission.ui.components.ScreenSection
import com.mansao.dicodingfundamentalsubmission.ui.components.EventColumnItem
import com.mansao.dicodingfundamentalsubmission.ui.screen.home.components.EventRowItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val itemLimit = 5
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                navigateToSettings = navigateToSettings
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(scaffoldPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                ScreenSection(title = "Upcoming Events") {
                    UpcomingEventUiState(
                        homeViewModel = homeViewModel,
                        context = context,
                        navigateToDetail = navigateToDetail,
                        itemLimit = itemLimit
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))

            }
            item {
                ScreenSection(title = "Finished Events") {
                    FinishedEventUiState(
                        homeViewModel = homeViewModel,
                        context = context,
                        navigateToDetail = navigateToDetail,
                        itemLimit = itemLimit
                    )
                }
            }
        }
    }
}

@Composable
fun UpcomingEventUiState(
    homeViewModel: HomeViewModel,
    context: Context,
    navigateToDetail: (Int) -> Unit,
    itemLimit: Int
) {
    homeViewModel.upcomingEventUiState.collectAsState().value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> UpcomingEventRow(
                events = uiState.data.listEvents,
                context = context,
                navigateToDetail = navigateToDetail,
                itemLimit = itemLimit
            )

            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Composable
fun FinishedEventUiState(
    homeViewModel: HomeViewModel,
    context: Context,
    navigateToDetail: (Int) -> Unit,
    itemLimit: Int
) {
    homeViewModel.finishedEventUiState.collectAsState().value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> FinishedEventList(
                events = uiState.data.listEvents,
                context = context,
                navigateToDetail = navigateToDetail,
                itemLimit = itemLimit
            )

            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Composable
fun UpcomingEventRow(
    events: List<EventDto>,
    context: Context,
    navigateToDetail: (Int) -> Unit,
    itemLimit: Int
) {
    LazyRow {
        items(events.take(itemLimit), key = { it.id }) { event ->
            EventRowItem(
                context = context,
                event = event,
                modifier = Modifier.clickable { navigateToDetail(event.id) })
        }
    }
}

@Composable
fun FinishedEventList(
    events: List<EventDto>,
    context: Context,
    navigateToDetail: (Int) -> Unit,
    itemLimit: Int
) {

    events.take(itemLimit).forEach { event ->
        EventColumnItem(context = context, event = event, modifier = Modifier.clickable {
            navigateToDetail(event.id)
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToSettings: () -> Unit
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "Dicoding Event",
                style = MaterialTheme.typography.displayMedium
            )
        },
        actions = {
            IconButton(onClick = { navigateToSettings() }) {
                Icon(imageVector = Icons.Outlined.Settings, contentDescription = null)
            }
        })
}