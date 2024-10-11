package com.mansao.dicodingfundamentalsubmission.data

import com.mansao.dicodingfundamentalsubmission.data.local.preferences.AppPreferences
import com.mansao.dicodingfundamentalsubmission.data.netwok.ApiService
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.DetailEventResponse
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.EventDto
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.ListEventResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DicodingEventRepository {
    suspend fun getActiveEvent(): ListEventResponse
    suspend fun getOneEvent(): ListEventResponse
    suspend fun getFinishedEvent(): ListEventResponse
    suspend fun searchEvent(query: String): ListEventResponse
    suspend fun getDetailEvent(id: Int): DetailEventResponse

    suspend fun saveIsDarkMode(newValue: Boolean)
    suspend fun savePushNotification(newValue: Boolean)

     fun getIsDarkMode(): Flow<Boolean>
     fun getPushNotification(): Flow<Boolean>
}

class DicodingEventRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appPreferences: AppPreferences
) : DicodingEventRepository {
    override suspend fun getActiveEvent(): ListEventResponse = apiService.getActiveEvent()
    override suspend fun getOneEvent(): ListEventResponse = apiService.getOneEvent()

    override suspend fun getFinishedEvent(): ListEventResponse = apiService.getFinishedEvent()

    override suspend fun searchEvent(query: String): ListEventResponse =
        apiService.searchEvent(query = query)

    override suspend fun getDetailEvent(id: Int): DetailEventResponse =
        apiService.getDetailEvent(id)


    override suspend fun saveIsDarkMode(newValue: Boolean)  = appPreferences.saveIsDarkMode(newValue)
    override suspend fun savePushNotification(newValue: Boolean)  = appPreferences.savePushNotification(newValue)
    override  fun getIsDarkMode(): Flow<Boolean>  = appPreferences.getIsDarkMode()
    override  fun getPushNotification(): Flow<Boolean>  = appPreferences.getIsPushNotification()
}