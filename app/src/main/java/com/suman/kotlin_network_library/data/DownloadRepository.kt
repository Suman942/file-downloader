package com.suman.kotlin_network_library.data

import android.os.Environment
import android.util.Log
import com.suman.kotlin_network_library.domain.DownloadStatus
import com.suman.kotlin_network_library.domain.DownloadUiModel
import com.suman.network_library.Downloader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(private val downloader: Downloader) {
    private val TAG = "DownloadRepository"
    private val activeDownloads = mutableMapOf<Int, DownloadUiModel>()

    private fun getDirectoryPath(): String{
        val downloadsDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )

        val appFolder = File(downloadsDir, "MyApp")

        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }

        val directoryPath = appFolder.absolutePath
        return directoryPath

    }


    // Default download list
    private val defaultDownloads = Utils.imageUrls.mapIndexed { index, url ->
        DownloadUiModel(
            id = -(index + 1),          // 👈 placeholder ID
            url = url,
            fileName = "download ${index}",
            status = DownloadStatus.IDLE,
        )
    }

    init {
        // Populate activeDownloads with defaults
        defaultDownloads.forEach { activeDownloads[it.id] = it }
    }


    private val _downloads =
        MutableStateFlow<List<DownloadUiModel>>(activeDownloads.values.toList())

    val downloads: StateFlow<List<DownloadUiModel>> =
        _downloads.asStateFlow()

    fun start(url: String, fileName: String) {
        Log.d(TAG,"start download: ")
        val request = downloader
            .newReqBuilder(
                url,
                getDirectoryPath(),
                fileName
            )
            .build()

        val id = downloader.enqueue(
            request,
            onStart = { id ->
                Log.d(TAG,"download started")
                updateStatus(id, DownloadStatus.DOWNLOADING)
            },
            onProgress = { id, value ->
                Log.d(TAG,"download on progress : $value")
                updateProgress(id, value, DownloadStatus.PROGRESS)
            },
            onPause = { id ->
                Log.d(TAG,"download onPause")
                updateStatus(id, DownloadStatus.PAUSED)
            },
            onResume = { id, downloadedBytes ->
                Log.d(TAG,"download onResume")
//                updateResume(id,downloadedBytes,DownloadStatus.RESUME)
                updateStatus(id, DownloadStatus.DOWNLOADING)
            },
            onCancel = { id ->
                Log.d(TAG,"download onCancel")
                updateStatus(id, DownloadStatus.CANCELLED)
            },
            onComplete = { id ->
                Log.d(TAG,"download onComplete")
                updateStatus(id, DownloadStatus.COMPLETED)
            },
            onError = { id, value ->
                Log.d(TAG,"download onError")
                updateStatus(id, DownloadStatus.ERROR)
            }
        )

        val existing = activeDownloads.values.find { it.url == url }

        if (existing != null) {
            activeDownloads.remove(existing.id)
            activeDownloads[id] =
                existing.copy(
                    id = id,
                    status = DownloadStatus.DOWNLOADING,
                    progress = 0
                )
        }


        emit()
    }

    fun pause(id: Int) = downloader.pause(id)
    fun resume(id: Int) = downloader.resume(id)
    fun cancel(id: Int) = downloader.cancel(id)

    private fun updateProgress(id: Int, progress: Int,status: DownloadStatus) {
        activeDownloads[id]?.let {
            activeDownloads[id] = it.copy(progress = progress, status = status)
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