package com.suman.network_library.internal

import com.suman.network_library.DownloaderConfig
import com.suman.network_library.local_storage.DownloadEntity
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

    companion object {

        internal fun fromEntity(
            entity: DownloadEntity,
            config: DownloaderConfig
        ): DownloadRequest {

            return DownloadRequest(
                url = entity.url,
                tag = entity.tag,
                dirPath = entity.dirPath,
                downloadId = entity.id,
                fileName = entity.fileName,
                readTimeOut = config.readTimeOut,
                connectTimeOut = config.connectionTimeOut
            ).apply {
                downloadedBytes = entity.downloadedBytes
                totalBytes = entity.totalBytes ?: 0
                lastModified = entity.lastModified ?: 0
                state = entity.status
            }
        }
    }


    internal var totalBytes: Long = 0
    internal var downloadedBytes: Long = 0
    internal var lastModified : Long = System.currentTimeMillis()
    internal lateinit var job: Job
    internal  var onStart: () -> Unit = {}
    internal  var onProgress: (value: Int) -> Unit = {}
    internal  var onPause: () -> Unit = {}
    internal var onResume: (value: Long) -> Unit = {}
    internal  var onCancel: () -> Unit = {}
    internal  var onComplete: () -> Unit = {}
    internal  var onError: (error: String?) -> Unit = {}
    internal var state = -1

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

