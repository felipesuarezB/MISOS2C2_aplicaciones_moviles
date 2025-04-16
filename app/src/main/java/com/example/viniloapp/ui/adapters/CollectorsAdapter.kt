package com.example.viniloapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.databinding.ItemCollectorBinding
import com.example.viniloapp.models.Collector

class CollectorAdapter : ListAdapter<Collector, CollectorAdapter.CollectorViewHolder>(com.example.viniloapp.ui.adapters.CollectorAdapter.CollectorDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val binding = ItemCollectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CollectorViewHolder(private val binding: ItemCollectorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            binding.collectorName.text = collector.name
            binding.collectorEmail.text = collector.email
        }
    }

    private class CollectorDiffCallback : DiffUtil.ItemCallback<Collector>() {
        override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
            return oldItem == newItem
        }
    }
}
