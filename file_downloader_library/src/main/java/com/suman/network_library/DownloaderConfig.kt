package com.suman.network_library

data class DownloaderConfig(
    val httpClient: HttpClient = DefaultHttpClient(),
    val connectionTimeOut : Int = Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS,
    val readTimeOut: Int = Constants.DEFAULT_READ_TIMEOUT_MILLIS
)