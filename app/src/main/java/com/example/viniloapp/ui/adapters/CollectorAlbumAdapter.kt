package com.example.viniloapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.databinding.ItemCollectorAlbumBinding
import com.example.viniloapp.models.CollectorAlbum
import com.example.viniloapp.R

class CollectorAlbumAdapter: ListAdapter<CollectorAlbum, CollectorAlbumAdapter.CollectorAlbumViewHolder>(CollectorAlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorAlbumViewHolder {
        val binding = DataBindingUtil.inflate<ItemCollectorAlbumBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_collector_album,
            parent,
            false
        )
        return CollectorAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorAlbumViewHolder, position: Int) {
        val album = getItem(position)
        holder.bind(album)
    }

    class CollectorAlbumViewHolder(private val binding: ItemCollectorAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collectorAlbum: CollectorAlbum) {
            binding.collectorAlbum = collectorAlbum
            binding.executePendingBindings()
        }
    }

    class CollectorAlbumDiffCallback : DiffUtil.ItemCallback<CollectorAlbum>() {
        override fun areItemsTheSame(oldItem: CollectorAlbum, newItem: CollectorAlbum): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CollectorAlbum, newItem: CollectorAlbum): Boolean {
            return oldItem == newItem
        }
    }
}
