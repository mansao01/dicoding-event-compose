package com.mansao.dicodingfundamentalsubmission.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.dicodingfundamentalsubmission.data.DicodingEventRepositoryImpl
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.ListEventResponse
import com.mansao.dicodingfundamentalsubmission.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repositoryImpl: DicodingEventRepositoryImpl) :
    ViewModel() {

    private val _upcomingEventUiState: MutableStateFlow<UiState<ListEventResponse>> =
        MutableStateFlow(UiState.Standby)
    val upcomingEventUiState = _upcomingEventUiState.asStateFlow()

    private val _finishedEventUiState: MutableStateFlow<UiState<ListEventResponse>> =
        MutableStateFlow(UiState.Standby)
    val finishedEventUiState = _finishedEventUiState.asStateFlow()

    init {
        getActiveEvent()
        getFinishedEvent()
    }
    private fun getActiveEvent() = viewModelScope.launch {
        _upcomingEventUiState.value = UiState.Loading
        try {
            val response = repositoryImpl.getActiveEvent()
            _upcomingEventUiState.value = UiState.Success(response)
        } catch (e: Exception) {
            _upcomingEventUiState.value = UiState.Error(e.message.toString())
        }
    }

    private fun getFinishedEvent() = viewModelScope.launch {
        _finishedEventUiState.value = UiState.Loading
        try {
            val response = repositoryImpl.getFinishedEvent()
            _finishedEventUiState.value = UiState.Success(response)
        } catch (e: Exception) {
            _finishedEventUiState.value = UiState.Error(e.message.toString())
        }
    }

}