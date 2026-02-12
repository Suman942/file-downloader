package com.suman.kotlin_network_library

data class DownloadUiModel(
    val id: Int,
    val url: String,
    val fileName: String,
    val progress: Int = 0,
    val status: DownloadStatus
)

enum class DownloadStatus {
    IDLE, DOWNLOADING, PAUSED, COMPLETED, CANCELLED, ERROR
}
