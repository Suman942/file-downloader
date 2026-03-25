package com.suman.downloader.network

import com.suman.downloader.internal.DownloadRequest

interface HttpClient {

    suspend fun connect(
        downloadRequest: DownloadRequest,
        onBytes: (bytesRead: Long, totalBytes: Long) -> Unit
    )
}