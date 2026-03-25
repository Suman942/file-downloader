package com.suman.downloader.local_storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper private constructor(private val dbHelper: SQLiteOpenHelper) {

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun initialise(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = DatabaseHelper(DownloadDbHelper(context.applicationContext))
                }
            }
        }

        fun getInstance(): DatabaseHelper {
            return INSTANCE
                ?: throw IllegalArgumentException(" DatabaseHelper is not initialized. Call initialize(context) first.")
        }
    }

    private val writableDb get() = dbHelper.writableDatabase
    fun insert(download: DownloadEntity) {
        val values = ContentValues().apply {
            put("id", download.id)
            put("url", download.url)
            put("dir_path", download.dirPath)
            put("file_name", download.fileName)
            put("downloaded_bytes", download.downloadedBytes)
            put("total_bytes", download.totalBytes)
            put("tag", download.tag)
            put("last_modified", download.lastModified)
            put("status", download.status)
            put("error", download.error)
            put("updated_at", download.updatedAt)
        }
        writableDb.insertWithOnConflict(
            "downloads",
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE

        )
    }

    private val readableDb get() = dbHelper.readableDatabase
    fun getDownloadedBytes(id: Int): Long {
        val cursor = readableDb.query(
            "downloads",
            arrayOf("downloaded_bytes"),
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use { cursor ->
            return if (cursor.moveToFirst()) cursor.getLong(0) else 0L
        }
    }

    fun updateProgress(id: Int, downloadedBytes: Long, status: Int? = null, totalBytes: Long,error: String?=null) {
        val values = ContentValues().apply {
            put("downloaded_bytes", downloadedBytes)
            status?.let {
                put("status", it)
            }
            put("updated_at", System.currentTimeMillis())
            put("total_bytes", totalBytes)
            put("error",error)
        }
        writableDb.update(
            "downloads",
            values,
            "id=?",
            arrayOf(id.toString())
        )

    }

    fun deleteDownload(id: Int) {
        writableDb.delete(
            "downloads",
            "id=?",
            arrayOf(id.toString())
        )
    }

    fun getPendingDownloads(): List<DownloadEntity> {
        val downloads = mutableListOf<DownloadEntity>()

        val cursor = readableDb.query(
            "downloads",
            null,// all column
            null,
            null,
            null,
            null,
            "updated_at DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                downloads.add(it.toDownloadEntity())
            }
        }
        return downloads
    }

}