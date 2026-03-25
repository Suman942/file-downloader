package com.suman.downloader.local_storage

internal fun android.database.Cursor.toDownloadEntity(): DownloadEntity {
    return DownloadEntity(
        id = getInt(getColumnIndexOrThrow("id")),
        url = getString(getColumnIndexOrThrow("url")),
        dirPath = getString(getColumnIndexOrThrow("dir_path")),
        fileName = getString(getColumnIndexOrThrow("file_name")),
        downloadedBytes = getLong(getColumnIndexOrThrow("downloaded_bytes")),
        totalBytes = getLong(getColumnIndexOrThrow("total_bytes")),
        tag = getString(getColumnIndexOrThrow("tag")),
        lastModified = getLong(getColumnIndexOrThrow("last_modified")),
        status = getInt(getColumnIndexOrThrow("status")),
        error = getString(getColumnIndexOrThrow("error")),
        updatedAt = getLong(getColumnIndexOrThrow("updated_at"))
    )
}
