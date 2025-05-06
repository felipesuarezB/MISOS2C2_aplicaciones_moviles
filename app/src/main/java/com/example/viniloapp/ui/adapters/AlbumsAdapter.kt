package com.example.viniloapp.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.R
import com.example.viniloapp.databinding.ItemAlbumBinding
import com.example.viniloapp.models.Album

class AlbumsAdapter : ListAdapter<Album, AlbumsAdapter.AlbumViewHolder>(AlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = DataBindingUtil.inflate<ItemAlbumBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_album,
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        holder.bind(album)

        holder.itemView.setOnClickListener { view ->
            val bundle = Bundle().apply {
                putInt("albumId", album.id)
            }
            Navigation.findNavController(view).navigate(
                R.id.action_albumListFragment_to_albumDetailFragment,
                bundle
            )
        }
    }

    class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.album = album
            binding.executePendingBindings()
        }
    }

    private class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
} 