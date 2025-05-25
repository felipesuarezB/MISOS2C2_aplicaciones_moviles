package com.example.viniloapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.databinding.ItemPerformerPrizeBinding
import com.example.viniloapp.models.PerformerPrize

class PerformerPrizesAdapter : ListAdapter<PerformerPrize, PerformerPrizesAdapter.PrizeViewHolder>(PrizeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrizeViewHolder {
        val binding = ItemPerformerPrizeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PrizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrizeViewHolder, position: Int) {
        val prize = getItem(position)
        Log.d("PerformerPrizesAdapter", "Binding prize at position $position: ${prize.id}")
        holder.bind(prize)
    }

    override fun submitList(list: List<PerformerPrize>?) {
        Log.d("PerformerPrizesAdapter", "Submitting list of ${list?.size} prizes")
        super.submitList(list)
    }

    class PrizeViewHolder(private val binding: ItemPerformerPrizeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(prize: PerformerPrize) {
            binding.prize = prize
            binding.executePendingBindings()
        }
    }

    class PrizeDiffCallback : DiffUtil.ItemCallback<PerformerPrize>() {
        override fun areItemsTheSame(oldItem: PerformerPrize, newItem: PerformerPrize): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PerformerPrize, newItem: PerformerPrize): Boolean {
            return oldItem == newItem
        }
    }
} 