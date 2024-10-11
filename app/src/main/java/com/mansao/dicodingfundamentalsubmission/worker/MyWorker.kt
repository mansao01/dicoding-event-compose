package com.mansao.dicodingfundamentalsubmission.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import com.mansao.dicodingfundamentalsubmission.R
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject


class MyWorker (
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return runBlocking {
            getEvent()
        }
    }

    private fun getEvent(): Result {
        Log.d(TAG, "get event: Start.....")
        Looper.prepare()
        val client = SyncHttpClient()
        val url = "https://event-api.dicoding.dev/events?active=-1&limit=1\n"
        Log.d(TAG, "get event: $url")

        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d(TAG, "Response: $result")

                    val jsonResponse = JSONObject(result)

                    val error = jsonResponse.getBoolean("error")
                    if (!error) {
                        val listEvents = jsonResponse.getJSONArray("listEvents")

                        if (listEvents.length() > 0) {
                            val event = listEvents.getJSONObject(0)

                            val name = event.getString("name")
                            val sum = event.getString("summary")

                            showNotification(name, sum)
                            resultStatus = Result.success()
                        } else {
                            Log.d(TAG, "No events found.")
                            resultStatus = Result.failure()
                        }
                    } else {
                        Log.d(TAG, "Error in fetching events.")
                        resultStatus = Result.failure()
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON Parsing error: ${e.message}")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showNotification("Get Event Failed", error?.message.toString())
                Log.d(TAG, "onFailure: Failed.....")
                resultStatus = Result.failure()

            }
        })
        return resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private val TAG = MyWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "mansao channel"
    }
}
//@HiltWorker
//class MyWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted workerParams: WorkerParameters,
//    private val dicodingEventRepository: DicodingEventRepositoryImpl
//) : Worker(context, workerParams) {
//
//    override fun doWork(): Result {
//        return runBlocking {
//            getEvent()
//        }
//    }
//
//    private suspend fun getEvent(): Result {
//        return try {
//
//            val result = dicodingEventRepository.getOneEvent()
//            val data = result.listEvents[0]
//            val title = data.name
//            val description = data.description
//            Log.d(TAG, "onSuccess: Done.....")
//            showNotification(title, description)
//            Result.success()
//        } catch (e: Exception) {
//            showNotification("Get Event Failed", e.message.toString())
//            Log.d(TAG, "onFailure: Failed.....")
//            Result.failure()
//        }
//    }
//
//    private fun showNotification(title: String, description: String?) {
//        val notificationManager =
//            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notification: NotificationCompat.Builder =
//            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
//                .setSmallIcon(R.drawable.baseline_notifications_active_24)
//                .setContentTitle(title)
//                .setContentText(description)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel =
//                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
//            notification.setChannelId(CHANNEL_ID)
//            notificationManager.createNotificationChannel(channel)
//        }
//        notificationManager.notify(NOTIFICATION_ID, notification.build())
//    }
//
//    companion object {
//        private val TAG = MyWorker::class.java.simpleName
//        const val NOTIFICATION_ID = 1
//        const val CHANNEL_ID = "channel_01"
//        const val CHANNEL_NAME = "mansao channel"
//    }
//}
