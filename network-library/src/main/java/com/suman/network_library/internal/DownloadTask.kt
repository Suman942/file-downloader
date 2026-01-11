package com.suman.network_library.internal

import android.util.Log
import com.suman.network_library.HttpClient
import com.suman.network_library.local_storage.DatabaseConstant
import com.suman.network_library.local_storage.DatabaseHelper
import com.suman.network_library.local_storage.DownloadEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadTask(
    private val downloadRequest: DownloadRequest,
    private val httpClient: HttpClient,
    private val databaseHelper: DatabaseHelper
) {

    suspend fun run(
        onStart: () -> Unit = {},
        onPause: () -> Unit = {},
        onComplete: () -> Unit = {},
        onProgress: (value: Int) -> Unit = {},
        onError: (error: String) -> Unit = {},
        onCancel: () -> Unit = {},
        onResume: (value: Long) -> Unit = {}
    ) {
        withContext(Dispatchers.IO) {
            try {
                // download request insert and start down loading
                insertRequest()
                onStart()

                var lastSavedProgress = -1
                // use of http client
                httpClient.connect(downloadRequest) { read, total ->
                    downloadRequest.totalBytes = total
                    downloadRequest.downloadedBytes = read
                    if (total > 0) {
                        val progress = ((read * 100) / total).toInt()
                        onProgress(progress)
                        if (progress > lastSavedProgress) {
                            lastSavedProgress = progress
                            Log.d("DownloadProgress","last progress: ${lastSavedProgress}")

                            updateRequest(
                                downloadRequest.downloadId,
                                DatabaseConstant.STATUS_DOWNLOADING,
                                downloadRequest.downloadedBytes,
                                total
                            )
                        }
                    }else{
                        Log.d("DownloadProgress","progress_53")

                        updateRequest(
                            downloadRequest.downloadId,
                            DatabaseConstant.STATUS_FAILED,
                            downloadRequest.downloadedBytes,
                            total
                        )
                    }
                }
                updateRequest(
                    downloadRequest.downloadId,
                    DatabaseConstant.STATUS_COMPLETED,
                    downloadRequest.downloadedBytes,
                    downloadRequest.totalBytes
                )
                databaseHelper.deleteDownload(downloadRequest.downloadId)
                onComplete()
            }catch (e: Exception){
                Log.d("DownloadProgress","error: ${e.message}")

//                updateRequest(downloadRequest.downloadId, DatabaseConstant.STATUS_FAILED,downloadRequest.downloadedBytes,downloadRequest.totalBytes)
            }

        }
    }
    private fun updateRequest(id: Int,status: Int,downloadedBytes: Long,totalBytes: Long){
        databaseHelper.updateProgress(
            id = id,
            status = status,
            downloadedBytes = downloadedBytes,
            totalBytes = totalBytes
        )
    }
    private fun insertRequest(){
        val downloadEntity = DownloadEntity(
            id = downloadRequest.downloadId,
            url = downloadRequest.url,
            dirPath = downloadRequest.dirPath,
            fileName = downloadRequest.fileName,
            downloadedBytes = downloadRequest.downloadedBytes,
            totalBytes = downloadRequest.totalBytes,
            tag = downloadRequest.tag,
            status = DatabaseConstant.STATUS_DOWNLOADING,
            lastModified = downloadRequest.lastModified,
            updatedAt = System.currentTimeMillis(),
            error = null
        )
        databaseHelper.insert(downloadEntity)
    }
}