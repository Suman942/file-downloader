package com.suman.network_library

import android.content.Context
import com.suman.network_library.internal.DownloadDispatchers
import com.suman.network_library.internal.DownloadRequest
import com.suman.network_library.internal.DownloadRequestQueue
import com.suman.network_library.local_storage.DatabaseHelper

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
                    Downloader(downloaderConfig).also { instance = it }
                }
            }
        }
    }

    private val requestQueue =
        DownloadRequestQueue(DownloadDispatchers(downloaderConfig.httpClient))

    fun newReqBuilder(url: String, dirPath: String, fileName: String): DownloadRequest.Builder {
        return DownloadRequest.Builder(url, dirPath, fileName)
            .readTimeOut(downloaderConfig.readTimeOut)
            .connectTimeOut(downloaderConfig.connectionTimeOut)
    }

    fun enqueue(
        request: DownloadRequest,
        onStart: () -> Unit = {},
        onPause: () -> Unit = {},
        onProgress: (value: Int) -> Unit = { _ -> },
        onError: (error: String?) -> Unit = { _ -> },
        onCancel: () -> Unit = {},
        onComplete: () -> Unit = {},
        onResume: (value: Long) -> Unit = { _ -> }

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