package com.suman.network_library

import com.suman.network_library.HttpClient
import com.suman.network_library.internal.DownloadRequest
import kotlinx.coroutines.delay

class FakeHttpClient : HttpClient {

    override suspend fun connect(
        downloadRequest: DownloadRequest,
        onBytes: (bytesRead: Long, totalBytes: Long) -> Unit
    ) {
        val total = 100L  // simulate 100 bytes total
        var downloaded = 0L

        while (downloaded < total) {
            delay(50) // simulate network delay
            downloaded += 10
            if (downloaded > total) downloaded = total
            onBytes(downloaded, total)
        }

        // Can throw exception to test failures:
        // throw Exception("Simulated network error")
    }
}
