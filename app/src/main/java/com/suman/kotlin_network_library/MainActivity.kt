package com.suman.kotlin_network_library

import android.os.Bundle
import android.os.Environment
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.suman.kotlin_network_library.databinding.ActivityMainBinding
import com.suman.network_library.Constants
import com.suman.network_library.Downloader
import com.suman.network_library.internal.DownloadRequest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var downloader: Downloader
    private lateinit var request: DownloadRequest
    private lateinit var request2: DownloadRequest

    private val downloadsPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
    private var currentDownloadId: Int? = null
    private var currentDownloadId2: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        downloader = (application as MyApplication).downloader

        binding.apply {
            downloadBtn1.setOnClickListener {
                startDownload()
            }
            cancelBtn1.setOnClickListener {
                currentDownloadId?.let {
                    downloader.cancel(it)
                }
            }
            pauseBtn1.setOnClickListener {
                currentDownloadId?.let {
                    downloader.pause(it)
                }
            }
            resumeBtn1.setOnClickListener {
                currentDownloadId?.let {
                    downloader.resume(it)
                }
            }

            //

            downloadBtn2.setOnClickListener {
                startDownload2()
            }
            cancelBtn2.setOnClickListener {
                currentDownloadId2?.let {
                    downloader.cancel(it)
                }
            }
            pauseBtn2.setOnClickListener {
                currentDownloadId2?.let {
                    downloader.pause(it)
                }
            }
            resumeBtn2.setOnClickListener {
                currentDownloadId2?.let {
                    downloader.resume(it)
                }
            }

        }


    }
    private fun startDownload2() {
        val url =
"https://images.pexels.com/photos/236047/pexels-photo-236047.jpeg?cs=srgb&dl=landscape-nature-sky-236047.jpg&fm=jpg"
       request2 = downloader.newReqBuilder(
            url,
            dirPath = downloadsPath,
            fileName = "$packageName _${System.currentTimeMillis()}"
        )
            .readTimeOut(Constants.DEFAULT_READ_TIMEOUT_MILLIS)
            .connectTimeOut(Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setTag("someTag")
            .build()

        currentDownloadId2 = downloader.enqueue(
            request2,
            onStart = {
                binding.textViewStatus2.text = "downloading..."
            },
            onProgress = {
                binding.textViewProgress2.text = "Progress: $it %"
                binding.progressBar2.progress = it
            },
            onPause = {
                binding.textViewStatus2.text = "onPause"
            },

            onCancel = {
                binding.textViewStatus2.text = "download cancelled"
                binding.progressBar2.progress = 0
                binding.textViewProgress2.text = "Progress: $0%"

            },
            onError = {
                binding.textViewStatus2.text = it
            },
            onComplete = {
                binding.textViewStatus2.text = "onCompleted \n${downloadsPath}"
            },
        )

    }


    private fun startDownload() {
         val url =
            "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEivjcWPthi-WHxcwoy7tZK8O4CVv66U55HhVtEzQJedml2pY3xEjX-C8CbtSiB-vZywGNEl05lAXSxkfoo5WBNfXtxabZ2RRNs8vD0IBDoCQfLKBmSaZTkYC8DseoyeklNgP1n8ffvodiQocbhP7Epjpgb162Ydn5lmyNE3PUVJq7l_pjkYB5rtMLbSwqI/s1600/theandroidshow-google-io-2025.png"
        request = downloader.newReqBuilder(
            url,
            dirPath = downloadsPath,
            fileName = "$packageName _${System.currentTimeMillis()}"
        )
            .readTimeOut(Constants.DEFAULT_READ_TIMEOUT_MILLIS)
            .connectTimeOut(Constants.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setTag("someTag")
            .build()

        currentDownloadId = downloader.enqueue(
            request,
            onStart = {
                binding.textViewStatus1.text = "downloading..."
            },
            onProgress = {
                binding.textViewProgress1.text = "Progress: $it %"
                binding.progressBar1.progress = it
            },
            onPause = {
                binding.textViewStatus1.text = "onPause"
            },

            onCancel = {
                binding.textViewStatus1.text = "download cancelled"
                binding.progressBar1.progress = 0
                binding.textViewProgress1.text = "Progress: $0%"

            },
            onError = {
                binding.textViewStatus1.text = it
            },
            onComplete = {
                binding.textViewStatus1.text = "onCompleted \n${downloadsPath}"
            },
        )

    }
}