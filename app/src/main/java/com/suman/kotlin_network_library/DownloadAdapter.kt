package com.suman.kotlin_network_library

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suman.kotlin_network_library.databinding.DownloadItemLayoutBinding
import com.suman.kotlin_network_library.di.ActivityScope
@ActivityScope
class DownloadAdapter :
    ListAdapter<DownloadUiModel, DownloadAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DownloadUiModel>() {
            override fun areItemsTheSame(
                oldItem: DownloadUiModel,
                newItem: DownloadUiModel
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: DownloadUiModel,
                newItem: DownloadUiModel
            ): Boolean = oldItem == newItem
        }
    }

    // 👉 Set from Fragment / Activity (after injection)
    var onDownload: (String, String) -> Unit = { _, _ -> }
    var onPause: (Int) -> Unit = {}
    var onResume: (Int) -> Unit = {}
    var onCancel: (Int) -> Unit = {}

    inner class ViewHolder(
        private val binding: DownloadItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DownloadUiModel) = with(binding) {

//            fileNameTxt.text = item.fileName
//            progressBar.progress = item.progress
//            progressTxt.text = "${item.progress}%"
            Log.d("MainActivity","adapter: ${item.status} ${item.progress}")

            when (item.status) {
                DownloadStatus.IDLE->{
                    downloadBtn1.setOnClickListener {
                        onDownload(item.url,"${System.currentTimeMillis()}")
                    }
                }
                DownloadStatus.PROGRESS->{
                    textViewStatus1.text = "Downloading"
                    progressBar1.progress = item.progress
                }
                DownloadStatus.DOWNLOADING -> {
                    downloadBtn1.text = "Downloading"
                    textViewStatus1.text = "Downloading"
                    downloadBtn1.isEnabled = true
                    downloadBtn1.setOnClickListener {
                        onPause(item.id)
                    }
                }

                DownloadStatus.PAUSED -> {
//                    downloadBtn1.text = "Resume"
                    textViewStatus1.text = "Paused"
                    downloadBtn1.isEnabled = true
                    downloadBtn1.setOnClickListener {
                        onResume(item.id)
                    }
                }

                DownloadStatus.COMPLETED -> {
                    downloadBtn1.text = "Downloaded"
                    textViewStatus1.text = "Downloaded"
                    progressBar1.progress = item.progress
                    textViewProgress1.text = "${item.progress}%"
                    downloadBtn1.isEnabled = false
                }

                DownloadStatus.CANCELLED,
                DownloadStatus.ERROR -> {
                    downloadBtn1.text = "Retry"
                    downloadBtn1.isEnabled = true
                    downloadBtn1.setOnClickListener {
                        onDownload(item.url, item.fileName)
                    }
                }

            }

            cancelBtn1.setOnClickListener {
                onCancel(item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DownloadItemLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
