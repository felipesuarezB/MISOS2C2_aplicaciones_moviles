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
import com.example.viniloapp.databinding.ItemArtistBinding
import com.example.viniloapp.models.Artist

class ArtistsAdapter : ListAdapter<Artist, ArtistsAdapter.ArtistViewHolder>(ArtistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = DataBindingUtil.inflate<ItemArtistBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_artist,
            parent,
            false
        )
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        holder.bind(artist)

        holder.itemView.setOnClickListener { view ->
            val bundle = Bundle().apply {
                putInt("artistId", artist.id)
            }
            Navigation.findNavController(view).navigate(
                R.id.action_artistListFragment_to_artistDetailFragment,
                bundle
            )
        }
    }

    class ArtistViewHolder(private val binding: ItemArtistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            binding.artist = artist
            binding.executePendingBindings()
        }
    }

    private class ArtistDiffCallback : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }
    }
} 