package com.suman.network_library.internal

internal data class DownloaderConfig(
    val maxRetries: Int = 3,
    val baseRetryDelayMillis: Long = 1000L
)