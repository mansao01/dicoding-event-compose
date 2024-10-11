package com.mansao.dicodingfundamentalsubmission.data.netwok

import com.mansao.dicodingfundamentalsubmission.data.netwok.response.DetailEventResponse
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.EventDto
import com.mansao.dicodingfundamentalsubmission.data.netwok.response.ListEventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(ApiConst.EVENTS)
    suspend fun getActiveEvent(
        @Query("active") active: Int = 1
    ): ListEventResponse

    @GET(ApiConst.EVENTS)
    suspend fun getOneEvent(
        @Query("active") active: Int = -1,
        @Query("limit") limit: Int = 1
    ): ListEventResponse

    @GET(ApiConst.EVENTS)
    suspend fun getFinishedEvent(
        @Query("active") active: Int = 0
    ): ListEventResponse

    @GET(ApiConst.DETAIL_EVENT)
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): DetailEventResponse

    @GET(ApiConst.EVENTS)
    suspend fun searchEvent(
        @Query("active") id: Int = -1,
        @Query("q") query: String
    ): ListEventResponse
}