package com.suman.downloader

import com.suman.downloader.network.DefaultHttpClient
import com.suman.downloader.network.HttpClient
import com.suman.downloader.utils.Constants

 data class DownloaderConfig(
     val httpClient: HttpClient = DefaultHttpClient(),
     val connectionTimeOut : Int = Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS,
     val readTimeOut: Int = Constants.DEFAULT_READ_TIMEOUT_MILLIS
)