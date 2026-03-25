package com.suman.downloader.local_storage

internal data class DownloadEntity(
    val id: Int,
    val url: String,
    val dirPath: String,
    val fileName: String,
    val downloadedBytes: Long,
    val totalBytes: Long?,
    val tag: String?,
    val lastModified: Long?,
    val status: Int,
    val error: String?,
    val updatedAt: Long
)
