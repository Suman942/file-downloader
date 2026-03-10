package com.suman.network_library.network

import com.suman.network_library.internal.DownloadRequest

interface HttpClient {

    suspend fun connect(
        downloadRequest: DownloadRequest,
        onBytes: (bytesRead: Long, totalBytes: Long) -> Unit
    )
}