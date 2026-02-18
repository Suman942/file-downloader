package com.suman.network_library.utils

import android.webkit.MimeTypeMap
import java.net.HttpURLConnection

internal object FileNameUtils {


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


    private val allowedExtensions = setOf(
        "jpg","jpeg","png","webp","gif",
        "mp4","mkv","avi","mov",
        "mp3","wav","aac",
        "pdf","doc","docx","xls","xlsx",
        "ppt","pptx","zip","rar","apk"
    )

    fun hasExtension(fileName: String): Boolean {
        val extension = getExtension(fileName)?.lowercase() ?: return false
        return extension in allowedExtensions
    }

    fun getExtension(fileName: String): String? {
        val cleanName = fileName
            .substringBefore('?')      // remove query params
            .substringBefore('#')      // remove fragments

        val lastDot = cleanName.lastIndexOf('.')

        // No dot OR dot is first char (.nomedia) OR dot is last char (file.)
        if (lastDot <= 0 || lastDot == cleanName.length - 1) {
            return null
        }

        return cleanName.substring(lastDot + 1).lowercase()
    }


}