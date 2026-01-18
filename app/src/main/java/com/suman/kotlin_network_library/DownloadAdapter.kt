package com.suman.kotlin_network_library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suman.kotlin_network_library.databinding.DownloadItemLayoutBinding
import com.suman.kotlin_network_library.di.ActivityScope

@ActivityScope
class DownloadAdapter :
    ListAdapter<String, DownloadAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
    var onDownloadClick : (String) -> Unit = {}

    inner class ViewHolder(
         private val binding: DownloadItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String)=with(binding) {
            downloadBtn1.setOnClickListener { onDownloadClick.invoke(item) }
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
