# File Downloader Library

A lightweight **Android file downloader library** built with **Kotlin, Coroutines, and HttpURLConnection**.

Supports **pause, resume, progress tracking, and multiple downloads** with a clean API.

---

# ✨ Features

- Download files using **HttpURLConnection**
- **Pause / Resume** downloads
- **Progress callbacks**
- **Cancel downloads**
- **Error handling**
- **Resume support using HTTP Range headers**
- **Lightweight – no heavy dependencies**
- Works with **Kotlin and Java**
- Works with Kotlin projects only ✅

---

## 📁 Project Structure

```
file_downloader_library
│
├── manifests
│
└── kotlin+java
    │
    └── com.suman.network_library
        │
        ├── internal
        │   ├── DownloadDispatchers
        │   ├── DownloaderConfig
        │   ├── DownloadRequest
        │   ├── DownloadRequestQueue
        │   ├── DownloadTask
        │   └── NetworkMonitor
        │
        ├── local_storage
        │   ├── DatabaseHelper
        │   ├── DownloadDbHelper
        │   ├── DownloadEntity
        │   ├── DownloadStates
        │   └── Mapper.kt
        │
        ├── network
        │
        ├── utils
        │   ├── Constants
        │   ├── FileNameUtils
        │   └── Utils.kt
        │
        ├── Downloader
        └── DownloaderConfig
```
## ⚙️ System Flow / Architecture

The downloader is designed as a queue-based, coroutine-driven system to efficiently manage multiple downloads with support for pause, resume, and progress tracking.

---

### 🔁 How it Works

- Download requests are created using the `Downloader` API  
- Each request is added to a **DownloadRequestQueue**  
- A dispatcher manages execution and controls parallel downloads  
- Each request is processed as a separate **DownloadTask**  
- Downloads run using **Kotlin Coroutines** for non-blocking execution  
- Progress and state are stored in a **local database**  
- On resume, downloads continue from the last saved byte using HTTP **Range headers**

---

### 🔄 Flow Summary
Request → Queue → Dispatcher → Task → Network → File System
↓
Database (Progress / State)

---

### ⚡ Key Highlights

- **Queue-based system** → Efficient request management  
- **Coroutine-powered** → Lightweight and asynchronous  
- **Parallel downloads** → Multiple files can download simultaneously  
- **Persistent state** → Supports resume even after interruption  
- **Clean separation** → API, queue, worker, and storage layers  

## 🎥 Demo

<p align="center">
  <img src="screenshots/demo.gif" width="300"/>
</p>

# 📦 Installation

## Step 1: Add JitPack repository

Add JitPack to your **root `build.gradle`**.

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

---

## Step 2: Add dependency

```gradle
implementation 'com.github.Suman942:file-downloader:1.0.2'
```

---

# 🚀 Initialize Downloader

```kotlin
val downloader = Downloader.create(context)
```

Recommended:
- Initialize in **Application class**
- Or provide using **Dagger / Hilt**

---

# 📥 Create Download Request

```kotlin
val request = Downloader.download(
    url = "...",
    destination = "...",
    fileName = "..."
)
```

### Example

```kotlin
val request = downloader.newReqBuilder(
    "https://example.com/file.mp4",
    Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    ).absolutePath,
    "video.mp4"
).build()
```

---

# ⬇️ Start Download

```kotlin
val id = downloader.enqueue(
    request,
    onStart = { id ->
        println("Download started: $id")
    },
    onProgress = { id, progress ->
        println("Progress: $progress%")
    },
    onPause = { id ->
        println("Download paused")
    },
    onResume = { id, bytes ->
        println("Resumed from $bytes bytes")
    },
    onCancel = { id ->
        println("Download cancelled")
    },
    onComplete = { id ->
        println("Download completed")
    },
    onError = { id, error ->
        println("Error: $error")
    }
)
```

---

# ⏸ Pause Download

```kotlin
downloader.pause(id)
```

---

# ▶ Resume Download

```kotlin
downloader.resume(id)
```

---

# ❌ Cancel Download

```kotlin
downloader.cancel(id)
```

---

## Why this library?

Android's default DownloadManager has limitations:
- Limited customization
- No fine-grained progress control
- Hard to integrate with modern architecture

This library solves these using coroutines and a custom queue system.

---

# 👨‍💻 Author

**Suman Shil**

GitHub:  
https://github.com/Suman942

---

# Medium
**https://medium.com/@suman.shil.942/building-a-lightweight-android-file-downloader-library-using-kotlin-coroutines-and-b51396cb0af3**
---
# LinkedIn
**https://www.linkedin.com/in/suman-shil-204177191/**
---
# 📄 License
MIT License



