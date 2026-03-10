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
implementation 'com.github.Suman942:file-downloader:1.0.1'
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
val request = downloader.newReqBuilder(
    url,
    destinationPath,
    fileName
).build()
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

# 📄 License

MIT License

---

# 👨‍💻 Author

**Suman Shil**

GitHub:  
https://github.com/Suman942
