package com.mansao.dicodingfundamentalsubmission.ui.screen.upcoming

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.dicodingfundamentalsubmission.R
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.EventDto
import com.mansao.dicodingfundamentalsubmission.ui.common.UiState
import com.mansao.dicodingfundamentalsubmission.ui.components.EventColumnItem

@Composable
fun UpcomingScreen(
    upcomingViewModel: UpcomingViewModel = hiltViewModel(),
    navigateToDetail: (Int) -> Unit
) {
    val context = LocalContext.current
    Scaffold { scaffoldPadding ->
        Column(modifier = Modifier.padding(scaffoldPadding)) {

            EventSearchBar(
                upcomingViewModel = upcomingViewModel,
                context = context,
                navigateToDetail = navigateToDetail,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            UpcomingEventUiState(
                upcomingViewModel = upcomingViewModel,
                context = context,
                navigateToDetail = navigateToDetail,
            )
        }
    }
}

@Composable
fun UpcomingEventUiState(
    upcomingViewModel: UpcomingViewModel,
    context: Context,
    navigateToDetail: (Int) -> Unit,
) {
    upcomingViewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> UpcomingEventList(
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
fun UpcomingEventList(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventSearchBar(
    modifier: Modifier = Modifier,
    upcomingViewModel: UpcomingViewModel,
    navigateToDetail: (Int) -> Unit,
    context: Context
) {
    var query by rememberSaveable { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { newQuery ->
                    query = newQuery
                    isActive = if (newQuery.isNotEmpty()) {
                        upcomingViewModel.searchEvent(newQuery)
                        true
                    } else {
                        upcomingViewModel.makeStandByState()
                        false
                    }
                },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text(text = stringResource(R.string.search_event)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (isActive && query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                query = ""
                                upcomingViewModel.makeStandByState()
                                upcomingViewModel.getUpcomingEvent()
                                expanded = false

                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                        }
                    }
                },
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
        content = {
            UpcomingEventUiState(
                upcomingViewModel = upcomingViewModel,
                context = context,
                navigateToDetail =navigateToDetail
            )
        }
    )
//    SearchBar(
//        query = query,
//        onQueryChange = { newQuery ->
//            query = newQuery
//            isActive = if (newQuery.isNotEmpty()) {
//                upcomingViewModel.searchEvent(newQuery)
//                true
//            } else {
//                upcomingViewModel.makeStandByState()
//                false
//            }
//        },
//        onSearch = { upcomingViewModel.searchEvent(query) },
//        onActiveChange = { isActive = it },
//        active = false,
//        placeholder = { Text(text = stringResource(R.string.search_event)) },
//        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
//        trailingIcon = {
//            if (isActive && query.isNotEmpty()) {
//                IconButton(
//                    onClick = {
//                        query = ""
//                        upcomingViewModel.makeStandByState()
//                        upcomingViewModel.getUpcomingEvent()
//
//                    }
//                ) {
//                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
//                }
//            }
//        },
//        content = {
//        },
//        modifier = modifier
//    )
}
