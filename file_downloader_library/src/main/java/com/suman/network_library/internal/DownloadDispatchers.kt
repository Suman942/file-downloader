package com.suman.network_library.internal

import android.util.Log
import com.suman.network_library.HttpClient
import com.suman.network_library.local_storage.DownloadStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DownloadDispatchers(private val httpClient: HttpClient) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private suspend fun execute(downloadReq: DownloadRequest) {

        DownloadTask(downloadReq, httpClient).run(
            onStart = {
                executeOnMainThread { downloadReq.onStart(it) }
            },
            onProgress = { id,value->
                executeOnMainThread { downloadReq.onProgress(id,value) } },
            onPause = {
                executeOnMainThread { downloadReq.onPause(it) } },
            onError = {id,value->
                executeOnMainThread { downloadReq.onError(id,value) } },
            onCancel = { executeOnMainThread { downloadReq.onCancel(it) } },
            onComplete = {
                executeOnMainThread { downloadReq.onComplete(it) }
            },
            onResume = {id,downloadedBytes->
                executeOnMainThread {
                    downloadReq.onResume(id,downloadedBytes)
                }
            }

        )
    }

    private fun executeOnMainThread(block: () -> Unit) {
        scope.launch {
            block()
        }
    }

    fun enqueue(downloadReq: DownloadRequest): Int {
        val job = scope.launch {
            execute(downloadReq)
        }
        downloadReq.job = job
        downloadReq.state = DownloadStates.STATUS_DOWNLOADING
        return downloadReq.downloadId
    }

    fun resume(downloadReq: DownloadRequest): Int {
        val job = scope.launch {
            execute(downloadReq)
        }
        downloadReq.job = job
        downloadReq.state = DownloadStates.STATUS_DOWNLOADING
        Log.d("DownloadProgress", "resume dispatchers: ${downloadReq.downloadedBytes}")
        return downloadReq.downloadId
    }

    fun cancel(request: DownloadRequest) {
        if (request.state == DownloadStates.STATUS_PAUSED) {
            request.onCancel.invoke(request.downloadId)
        } else {
            request.state = DownloadStates.STATUS_FAILED
            request.job.cancel()
        }
    }

    fun pause(request: DownloadRequest) {
        if (request.state == DownloadStates.STATUS_FAILED) {
            request.onPause.invoke(request.downloadId)
        } else {
            request.state = DownloadStates.STATUS_PAUSED
            request.job.cancel()
        }
    }


}