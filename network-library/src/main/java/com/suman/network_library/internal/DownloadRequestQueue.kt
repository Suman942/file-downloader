package com.suman.network_library.internal

import com.suman.network_library.local_storage.DatabaseHelper

class DownloadRequestQueue(
    private val dispatchers: DownloadDispatchers,
    private val databaseHelper: DatabaseHelper
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
            it.onResume.invoke(databaseHelper.getDownloadedBytes(id))
            dispatchers.enqueue(it)
        }
    }

    fun cancel(id: Int) {
        idRequestMap[id]?.let {
            dispatchers.cancel(it)
        }
        idRequestMap.remove(id)

    }


}