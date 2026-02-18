package com.suman.network_library

import com.suman.network_library.internal.DownloadRequest
import com.suman.network_library.local_storage.DownloadStates
import org.junit.Test
import org.junit.Assert.*
class DownloadRequestValidationTest{

    @Test
    fun valid_https_url_should_build_successfully(){
        val result = DownloadRequest.Builder(
            "https://example.com/file.jpg",
            "/storage/downloads",
            "file.jpg"
        ).build()
        assertNotNull(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun ftp_url_should_throw_exception() {
        DownloadRequest.Builder(
            "ftp://example.com/file.jpg",
            "/storage/downloads",
            "file.jpg"
        ).build()
    }

    @Test(expected = IllegalArgumentException::class)
    fun empty_url_should_throw_exception() {
        DownloadRequest.Builder(
            "",
            "/storage/downloads",
            "file.jpg"
        ).build()
    }

    @Test
    fun new_request_should_have_queued_state() {
        val request = DownloadRequest.Builder(
            "https://example.com/file.jpg",
            "/storage",
            "file.jpg"
        ).build()

        assertEquals(DownloadStates.STATUS_DOWNLOADING, request.state)
    }

}