package com.suman.network_library

import android.util.Log
import com.suman.network_library.internal.DownloadRequest
import com.suman.network_library.utils.FileNameUtils.detectFileName
import com.suman.network_library.utils.FileNameUtils.hasExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.coroutineContext


class DefaultHttpClient : HttpClient {

    override suspend fun connect(
        downloadRequest: DownloadRequest,
        onBytes: (bytesRead: Long, totalBytes: Long) -> Unit
    ) = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            val url = URL(downloadRequest.url)
            connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = downloadRequest.connectTimeOut
                readTimeout = downloadRequest.readTimeOut
                requestMethod = "GET"
                doInput = true

                // resume support
                if (downloadRequest.downloadedBytes > 0) {
                    setRequestProperty("Range", "bytes=${downloadRequest.downloadedBytes}-")
                }

                connect()
            }
            val contentLength = connection.contentLength

            if (connection.responseCode != HttpURLConnection.HTTP_OK &&
                connection.responseCode != HttpURLConnection.HTTP_PARTIAL
            ) {
                throw Exception("HTTP error code: ${connection.responseCode}")
            }

            val hasExtension = hasExtension(downloadRequest.fileName)
            val fileName = if (hasExtension) downloadRequest.fileName else {
                detectFileName(downloadRequest.url, connection, downloadRequest.fileName)
            }

            val file = File(downloadRequest.dirPath, fileName)

            // Server ignored Range → restart
            if (
                downloadRequest.downloadedBytes > 0 &&
                connection.responseCode == HttpURLConnection.HTTP_OK
            ) {
                downloadRequest.downloadedBytes = 0
                file.delete()
            }

            val totalBytes = if (connection.responseCode == HttpURLConnection.HTTP_PARTIAL) {
                downloadRequest.downloadedBytes + contentLength
            } else {
                contentLength
            }
            downloadRequest.totalBytes

            connection.inputStream.use { inputStream ->
                file.outputStream().use { outputStream ->
                    copyInputStreamProgress(
                        inputStream,
                        outputStream,
                        downloadRequest.downloadedBytes,
                        totalBytes.toLong(),
                        onBytes
                    )
                }
            }

        } finally {
            connection?.disconnect()
        }

    }

    private suspend fun copyInputStreamProgress(
        input: InputStream,
        output: OutputStream,
        downloadedBytes: Long,
        totalBytes: Long,
        onBytes: (Long, Long) -> Unit
    ) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytesRead: Int
        var tempDownloadedBytes = downloadedBytes

        while (true) {
            coroutineContext.ensureActive()
            bytesRead = input.read(buffer)
            if (bytesRead == -1) break

            output.write(buffer, 0, bytesRead)
            tempDownloadedBytes += bytesRead

            onBytes(tempDownloadedBytes, totalBytes)
        }
    }


}