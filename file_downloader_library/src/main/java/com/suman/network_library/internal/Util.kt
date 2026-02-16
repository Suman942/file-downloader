package com.suman.network_library.internal

import android.webkit.MimeTypeMap
import java.net.HttpURLConnection

internal object Util {

    fun detectFileName(
        url: String,
        connection: HttpURLConnection?,
        baseName: String = "download"
    ): String {

        // 1️⃣ Try from Content-Disposition header (best case)
        val disposition = connection?.getHeaderField("Content-Disposition")
        disposition?.let {
            val regex = Regex("filename=\"?([^\"]+)\"?")
            val match = regex.find(it)
            val fileName = match?.groupValues?.get(1)
            if (!fileName.isNullOrEmpty()) {
                return fileName
            }
        }

        // 2️⃣ Try from MIME type
        val mimeType = connection?.contentType
        val extensionFromMime = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType)

        if (!extensionFromMime.isNullOrEmpty()) {
            return "$baseName.$extensionFromMime"
        }

        // 3️⃣ Try from URL
        val extensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(url)
        if (!extensionFromUrl.isNullOrEmpty()) {
            return "$baseName.$extensionFromUrl"
        }

        // 4️⃣ Fallback
        return "$baseName.bin"
    }

    fun hasExtension(fileName: String): Boolean {
        val extension = fileName.substringAfterLast('.', "")
        return extension.isNotEmpty() && extension.length <= 10
    }


}