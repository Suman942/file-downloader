package com.suman.network_library

import android.content.Context
import android.util.Log
import com.suman.network_library.internal.DownloadDispatchers
import com.suman.network_library.internal.DownloadRequest
import com.suman.network_library.internal.DownloadRequestQueue
import com.suman.network_library.internal.NetworkMonitor
import com.suman.network_library.local_storage.DatabaseHelper
import com.suman.network_library.local_storage.DownloadStates

class Downloader private constructor(private val downloaderConfig: DownloaderConfig) {

    companion object {

        @Volatile
        private var instance: Downloader? = null
        fun create(
            context: Context,
            downloaderConfig: DownloaderConfig = DownloaderConfig()
        ): Downloader {
            return instance ?: synchronized(this) {
                instance ?: run {
                    DatabaseHelper.initialise(context)
                    val downloader = Downloader(downloaderConfig)
                    instance = downloader
                    downloader.resumePendingDownloads()
                    downloader.startNetworkMonitor(context)
                    downloader
//                    Downloader(downloaderConfig).also {
//                        instance = it
//                        it.resumePendingDownloads()
//                        it
//                    }
                }
            }
        }
    }

    private fun startNetworkMonitor(context: Context) {

        NetworkMonitor(context) {
            Log.d("Downloader", "Network restored → resuming downloads")
//            resumeNetworkPausedDownloads()
            resumePendingDownloads()
        }.register()
    }

    private fun resumePendingDownloads() {
        val pending = DatabaseHelper.getInstance()
            .getPendingDownloads()

        pending.forEach {
            val request = DownloadRequest.fromEntity(
                entity = it, config = downloaderConfig
            )
            if (request.state == DownloadStates.STATUS_PAUSED) {
                DatabaseHelper.getInstance().deleteDownload(request.downloadId)
            }
            requestQueue.enqueue(
                request
            )
        }
    }


    //    private val requestQueue =
//        DownloadRequestQueue(DownloadDispatchers(downloaderConfig.httpClient))
    private val requestQueue by lazy {
        DownloadRequestQueue(DownloadDispatchers(downloaderConfig.httpClient))
    }

    fun newReqBuilder(url: String, dirPath: String, fileName: String): DownloadRequest.Builder {
        return DownloadRequest.Builder(url, dirPath, fileName)
            .readTimeOut(downloaderConfig.readTimeOut)
            .connectTimeOut(downloaderConfig.connectionTimeOut)
    }

    fun enqueue(
        request: DownloadRequest,
        onStart: (Int) -> Unit = { _ -> },
        onPause: (Int) -> Unit = {},
        onProgress: (Int, Int) -> Unit = { _, _ -> },
        onError: (Int, String?) -> Unit = { _, _ -> },
        onCancel: (Int) -> Unit = {},
        onComplete: (Int) -> Unit = {},
        onResume: (Int, Long) -> Unit = { _, _ -> }

    ): Int {
        request.onStart = onStart
        request.onPause = onPause
        request.onProgress = onProgress
        request.onComplete = onComplete
        request.onError = onError
        request.onCancel = onCancel
        request.onResume = onResume
        return requestQueue.enqueue(request)
    }

    fun cancel(id: Int) {
        requestQueue.cancel(id)
    }

    fun pause(id: Int) {
        requestQueue.pause(id)
    }

    fun resume(id: Int) {
        requestQueue.resume(id)
    }
}