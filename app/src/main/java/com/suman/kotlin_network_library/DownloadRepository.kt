package com.suman.kotlin_network_library

import android.os.Environment
import com.suman.network_library.Downloader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(private val downloader: Downloader) {

    private val activeDownloads = mutableMapOf<Int, DownloadUiModel>()

    private val _downloads =
        MutableStateFlow<List<DownloadUiModel>>(emptyList())

    val downloads: StateFlow<List<DownloadUiModel>> =
        _downloads.asStateFlow()

    fun start(url: String, fileName: String) {
        val request = downloader
            .newReqBuilder(
                url,
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,
                fileName
            )
            .build()

        val id = downloader.enqueue(
            request,
            onStart = { id ->
                updateStatus(id, DownloadStatus.DOWNLOADING)
            },
            onProgress = { id, value ->
                updateProgress(id, value)
            },
            onPause = { id ->
                updateStatus(id, DownloadStatus.PAUSED)
            },
            onResume = { id, value ->
                updateStatus(id, DownloadStatus.DOWNLOADING)
            },
            onCancel = { id ->
                updateStatus(id, DownloadStatus.CANCELLED)
            },
            onComplete = { id ->
                updateStatus(id, DownloadStatus.COMPLETED)
            },
            onError = { id, value ->
                updateStatus(id, DownloadStatus.ERROR)
            }
        )

        activeDownloads[id] = DownloadUiModel(
            id = id,
            url = url,
            fileName = fileName,
            status = DownloadStatus.DOWNLOADING
        )

        emit()
    }

    fun pause(id: Int) = downloader.pause(id)
    fun resume(id: Int) = downloader.resume(id)
    fun cancel(id: Int) = downloader.cancel(id)

    private fun updateProgress(id: Int, progress: Int) {
        activeDownloads[id]?.let {
            activeDownloads[id] = it.copy(progress = progress)
            emit()
        }
    }

    private fun updateStatus(id: Int, status: DownloadStatus) {
        activeDownloads[id]?.let {
            activeDownloads[id] = it.copy(status = status)
            emit()
        }
    }

    private fun emit() {
        _downloads.value = activeDownloads.values.toList()
    }
}