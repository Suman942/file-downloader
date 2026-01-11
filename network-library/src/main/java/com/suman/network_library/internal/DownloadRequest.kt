package com.suman.network_library.internal

import com.suman.network_library.utils.getUniqueId
import kotlinx.coroutines.Job

class DownloadRequest private constructor(
    internal val url: String,
    internal val tag: String?,
    internal val dirPath: String,
    internal val downloadId: Int,
    internal val fileName: String,
    internal val readTimeOut: Int,
    internal val connectTimeOut: Int
) {
    internal var totalBytes: Long = 0
    internal var downloadedBytes: Long = 0
    internal var lastModified : Long = System.currentTimeMillis()
    internal lateinit var job: Job
    internal lateinit var onStart: () -> Unit
    internal lateinit var onProgress: (value: Int) -> Unit
    internal lateinit var onPause: () -> Unit
    internal lateinit var onResume: (value: Long) -> Unit
    internal lateinit var onCancel: () -> Unit
    internal lateinit var onComplete: () -> Unit
    internal lateinit var onError: (error: String?) -> Unit

    data class Builder(
        val url: String,
        val dirPath: String,
        val fileName: String
    ) {
        private var tag: String? = null
        private var readTimeOut: Int = 0
        private var connectTimeOut: Int = 0

//        fun setTag(tag: String): Builder{
//            this.tag = tag
//            return this
//        }
        // above code can be written as below
        fun setTag(tag: String) = apply {
            this.tag = tag
        }

        fun readTimeOut(timeOut: Int) = apply {
            this.readTimeOut = timeOut
        }

        fun connectTimeOut(timeout: Int) = apply {
            this.connectTimeOut = timeout
        }

        fun build(): DownloadRequest = DownloadRequest(
            url = url,
            tag = tag,
            dirPath = dirPath,
            downloadId = getUniqueId(url,dirPath,fileName),
            fileName =fileName,
            readTimeOut = readTimeOut,
            connectTimeOut = connectTimeOut
        )
    }
}

