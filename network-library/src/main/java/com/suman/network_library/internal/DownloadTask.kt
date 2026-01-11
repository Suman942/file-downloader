package com.suman.network_library.internal

import android.util.Log
import com.suman.network_library.HttpClient
import com.suman.network_library.local_storage.DatabaseHelper
import com.suman.network_library.local_storage.DownloadEntity
import com.suman.network_library.local_storage.DownloadStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class DownloadTask(
    private val downloadRequest: DownloadRequest,
    private val httpClient: HttpClient
) {
    private val databaseHelper: DatabaseHelper = DatabaseHelper.getInstance()
    suspend fun run(
        onStart: () -> Unit = {},
        onPause: () -> Unit = {},
        onComplete: () -> Unit = {},
        onProgress: (value: Int) -> Unit = {},
        onError: (error: String?) -> Unit = {},
        onCancel: () -> Unit = {},
        onResume: (value: Long) -> Unit = {}
    ) {
        withContext(Dispatchers.IO) {
            try {
                // download request insert and start down loading
                val isResume = downloadRequest.downloadedBytes > 0
                Log.d("DownloadProgress", "resume task: ${downloadRequest.downloadedBytes}")

                insertRequest()

                if (isResume) {
                    onResume(downloadRequest.downloadedBytes)
                } else {
                    onStart()
                }
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

                            updateRequest(
                                downloadRequest.downloadId,
                                DownloadStates.STATUS_DOWNLOADING,
                                downloadRequest.downloadedBytes,
                                total
                            )
                        }
                    } else {
                        updateRequest(
                            downloadRequest.downloadId,
                            DownloadStates.STATUS_FAILED,
                            downloadRequest.downloadedBytes,
                            total
                        )
                    }
                }
                updateRequest(
                    downloadRequest.downloadId,
                    DownloadStates.STATUS_COMPLETED,
                    downloadRequest.downloadedBytes,
                    downloadRequest.totalBytes
                )
//                databaseHelper.deleteDownload(downloadRequest.downloadId)
                onComplete()
            }catch (e: CancellationException){
                e.stackTrace
                when (downloadRequest.state) {
                    DownloadStates.STATUS_FAILED -> {
                        onCancel()
                        updateRequest(
                            downloadRequest.downloadId,
                            DownloadStates.STATUS_FAILED,
                            downloadRequest.downloadedBytes,
                            downloadRequest.totalBytes
                        )
                    }

                    DownloadStates.STATUS_PAUSED -> {
                        onPause()
                        updateRequest(
                            downloadRequest.downloadId,
                            DownloadStates.STATUS_PAUSED,
                            downloadRequest.downloadedBytes,
                            downloadRequest.totalBytes
                        )

                    }
                }

            }
            catch (e: Exception) {
                onError(e.message)
            }

        }
    }

    private fun updateRequest(id: Int, status: Int, downloadedBytes: Long, totalBytes: Long) {
        databaseHelper.updateProgress(
            id = id,
            status = status,
            downloadedBytes = downloadedBytes,
            totalBytes = totalBytes
        )
    }

    private fun insertRequest() {
        val downloadEntity = DownloadEntity(
            id = downloadRequest.downloadId,
            url = downloadRequest.url,
            dirPath = downloadRequest.dirPath,
            fileName = downloadRequest.fileName,
            downloadedBytes = downloadRequest.downloadedBytes,
            totalBytes = downloadRequest.totalBytes,
            tag = downloadRequest.tag,
            status = DownloadStates.STATUS_DOWNLOADING,
            lastModified = downloadRequest.lastModified,
            updatedAt = System.currentTimeMillis(),
            error = null
        )
        databaseHelper.insert(downloadEntity)
    }
}