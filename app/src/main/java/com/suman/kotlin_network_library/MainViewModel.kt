package com.suman.kotlin_network_library

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: DownloadRepository): ViewModel() {

    /**
     * Expose downloads list to UI
     */
    val downloads: StateFlow<List<DownloadUiModel>> =
        repository.downloads

    /**
     * Start a new download
     */
    fun startDownload(url: String, fileName: String) {
        repository.start(url, fileName)
    }

    /**
     * User actions
     */
    fun pauseDownload(id: Int) {
        repository.pause(id)
    }

    fun resumeDownload(id: Int) {
        repository.resume(id)
    }

    fun cancelDownload(id: Int) {
        repository.cancel(id)
    }


}