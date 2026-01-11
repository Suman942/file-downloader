package com.suman.network_library.internal

import com.suman.network_library.HttpClient
import com.suman.network_library.local_storage.DatabaseConstant
import com.suman.network_library.local_storage.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DownloadDispatchers(private val httpClient: HttpClient,
    private val databaseHelper: DatabaseHelper) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
//    private val databaseHelper: DatabaseHelper = DatabaseHelper.getInstance()

    fun enqueue(downloadReq: DownloadRequest): Int {
        val job = scope.launch {
            execute(downloadReq)
        }
        downloadReq.job = job
        return downloadReq.downloadId
    }

    private suspend fun execute(downloadReq: DownloadRequest) {

        DownloadTask(downloadReq, httpClient,databaseHelper).run(
            onStart = {
                executeOnMainThread { downloadReq.onStart() }
            },
            onProgress = { executeOnMainThread { downloadReq.onProgress(it) } },
            onPause = { executeOnMainThread { downloadReq.onPause() } },
            onError = { executeOnMainThread { downloadReq.onError(it) } },
            onCancel = {executeOnMainThread { downloadReq.onCancel() }},
            onComplete = {
                executeOnMainThread { downloadReq.onComplete() }
            },
            onResume = {
                executeOnMainThread { downloadReq.onResume(databaseHelper.getDownloadedBytes(downloadReq.downloadId)) }
            }

        )
    }

    private fun executeOnMainThread(block: () -> Unit) {
        scope.launch {
            block()
        }
    }

    fun cancel(request: DownloadRequest) {
        if (request.state == DatabaseConstant.STATUS_PAUSED){
            request.onCancel.invoke()
        }else {
            request.state = DatabaseConstant.STATUS_FAILED
            request.job.cancel()
        }
    }

    fun pause(request: DownloadRequest) {
        if (request.state == DatabaseConstant.STATUS_FAILED){
            request.onPause.invoke()
        }else {
            request.state = DatabaseConstant.STATUS_PAUSED
            request.job.cancel()
        }
    }

    fun cancelAll() {
        scope.cancel()
    }
}