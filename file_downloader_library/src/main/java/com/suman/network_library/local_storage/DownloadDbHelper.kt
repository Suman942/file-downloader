package com.suman.network_library.local_storage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DownloadDbHelper (
    context: Context
) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_DOWNLOADS)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS downloads")
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "downloads.db"
        private const val DB_VERSION = 1

        private const val CREATE_TABLE_DOWNLOADS = """
            CREATE TABLE downloads (
            id INTEGER PRIMARY KEY,
            url TEXT NOT NULL,
            dir_path TEXT NOT NULL,
            file_name TEXT NOT NULL,
            downloaded_bytes INTEGER NOT NULL,
            total_bytes INTEGER,
            tag TEXT,
            last_modified INTEGER,
            status INTEGER NOT NULL,
            error TEXT,
            updated_at INTEGER NOT NULL
            )"""
    }


}