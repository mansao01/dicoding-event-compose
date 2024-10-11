package com.mansao.dicodingfundamentalsubmission.ui.screen.upcoming

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
class UpcomingViewModel @Inject constructor(private val repositoryImpl: DicodingEventRepositoryImpl) :
    ViewModel() {

    private val _uiState: MutableStateFlow<UiState<ListEventResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState = _uiState.asStateFlow()

    init {
        getUpcomingEvent()
    }

    fun makeStandByState(){
        _uiState.value = UiState.Standby
    }

    fun getUpcomingEvent() = viewModelScope.launch {
        _uiState.value = UiState.Loading
        try {
            val response = repositoryImpl.getActiveEvent()
            _uiState.value = UiState.Success(response)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }

    fun searchEvent(query: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        try {
            val response = repositoryImpl.searchEvent(query)
            _uiState.value = UiState.Success(response)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }
}