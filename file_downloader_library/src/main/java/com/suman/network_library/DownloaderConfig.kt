package com.suman.network_library

import com.suman.network_library.network.DefaultHttpClient
import com.suman.network_library.network.HttpClient
import com.suman.network_library.utils.Constants

 data class DownloaderConfig(
     val httpClient: HttpClient = DefaultHttpClient(),
     val connectionTimeOut : Int = Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS,
     val readTimeOut: Int = Constants.DEFAULT_READ_TIMEOUT_MILLIS
)