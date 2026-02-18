package com.suman.kotlin_network_library.presentation.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.suman.kotlin_network_library.data.DownloadRepository
import com.suman.kotlin_network_library.domain.DownloadUiModel
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
        Log.d("TAG","start download view model")
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