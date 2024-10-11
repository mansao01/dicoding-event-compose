package com.mansao.dicodingfundamentalsubmission.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.dicodingfundamentalsubmission.data.DicodingEventRepositoryImpl
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.DetailEventResponse
import com.mansao.dicodingfundamentalsubmission.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repositoryImpl: DicodingEventRepositoryImpl) :
    ViewModel() {
    private val _uiState: MutableStateFlow<UiState<DetailEventResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState = _uiState.asStateFlow()

    fun getDetail(id: Int) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        try {
            val result = repositoryImpl.getDetailEvent(id)
            _uiState.value = UiState.Success(result)
        }catch (e:Exception){
            _uiState.value = UiState.Error(e.message.toString())

        }

    }
}