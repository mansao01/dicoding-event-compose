package com.mansao.dicodingfundamentalsubmission

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.impl.Scheduler.MAX_SCHEDULER_LIMIT
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Inject

@HiltAndroidApp
class DicodingEventApplication : Application()
//    , Configuration.Provider {
//
//    @EntryPoint
//    @InstallIn(SingletonComponent::class)
//    interface HiltWorkerFactoryEntryPoint {
//        fun workerFactory(): HiltWorkerFactory
//    }
//
//    override val workManagerConfiguration: Configuration = Configuration.Builder()
//        .setExecutor(Dispatchers.Default.asExecutor())
//        .setWorkerFactory(EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory())
//        .setTaskExecutor(Dispatchers.Default.asExecutor())
//        .build()
//}


