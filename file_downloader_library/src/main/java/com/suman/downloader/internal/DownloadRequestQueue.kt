package com.suman.downloader.internal

class DownloadRequestQueue(
    private val dispatchers: DownloadDispatchers
) {

    private val idRequestMap: HashMap<Int, DownloadRequest> = hashMapOf()
    fun enqueue(downloadRequest: DownloadRequest): Int {
        idRequestMap[downloadRequest.downloadId] = downloadRequest
        return dispatchers.enqueue(downloadRequest)
    }

    fun pause(id: Int) {
        idRequestMap[id]?.let {
            dispatchers.pause(it)
        }
    }

    fun resume(id: Int) {
        idRequestMap[id]?.let {
            dispatchers.resume(it)
        }
    }

    fun cancel(id: Int) {
        idRequestMap[id]?.let {
            dispatchers.cancel(it)
        }
        idRequestMap.remove(id)

    }


}