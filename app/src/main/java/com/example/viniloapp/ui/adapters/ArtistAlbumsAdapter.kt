package com.example.viniloapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.databinding.ItemArtistAlbumBinding
import com.example.viniloapp.models.Album

class ArtistAlbumsAdapter : ListAdapter<Album, ArtistAlbumsAdapter.AlbumViewHolder>(AlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemArtistAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        Log.d("ArtistAlbumsAdapter", "Binding album at position $position: ${album.name}")
        holder.bind(album)
    }

    override fun submitList(list: List<Album>?) {
        Log.d("ArtistAlbumsAdapter", "Submitting list of ${list?.size} albums")
        super.submitList(list)
    }

    class AlbumViewHolder(private val binding: ItemArtistAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.album = album
            binding.executePendingBindings()
        }
    }

    class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
} 