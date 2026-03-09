package com.suman.kotlin_network_library.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suman.kotlin_network_library.databinding.DownloadItemLayoutBinding
import com.suman.kotlin_network_library.di.ActivityScope
import com.suman.kotlin_network_library.domain.DownloadStatus
import com.suman.kotlin_network_library.domain.DownloadUiModel

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

            textViewTitle1.text = item.fileName
            when (item.status) {
                DownloadStatus.IDLE->{

                }
                DownloadStatus.PROGRESS->{
                    textViewStatus1.text = "Downloading"
                    textViewProgress1.text = "Progress: ${item.progress}%"
                    progressBar1.progress = item.progress
                }
                DownloadStatus.DOWNLOADING -> {
                    textViewStatus1.text = "Downloading"
                    downloadBtn1.isEnabled = true
                }

                DownloadStatus.PAUSED -> {
                    textViewStatus1.text = "Paused"
                    downloadBtn1.isEnabled = true
                }

                DownloadStatus.RESUME ->{
                    textViewStatus1.text = "Resume"
                    downloadBtn1.isEnabled = false
                }

                DownloadStatus.COMPLETED -> {
                    textViewStatus1.text = "Downloaded"
                    progressBar1.progress = item.progress
                    textViewProgress1.text = "Progress: ${item.progress}%"
                }

                DownloadStatus.CANCELLED,
                DownloadStatus.ERROR -> {
                    textViewStatus1.text = "Error"
                    downloadBtn1.isEnabled = true

                }

            }

            downloadBtn1.setOnClickListener {
                onDownload(item.url,"${item.fileName}")
            }
            cancelBtn1.setOnClickListener {
                onCancel(item.id)
            }
            pauseBtn1.setOnClickListener {
                onPause(item.id)
            }
            resumeBtn1.setOnClickListener {
                onResume(item.id)
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