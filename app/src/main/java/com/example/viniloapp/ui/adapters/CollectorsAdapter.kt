package com.example.viniloapp.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.R
import com.example.viniloapp.databinding.CollectorItemBinding
import com.example.viniloapp.models.Collector

class CollectorsAdapter : ListAdapter<Collector, CollectorsAdapter.CollectorViewHolder>(CollectorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val binding = DataBindingUtil.inflate<CollectorItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.collector_item,
            parent,
            false
        )
        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        val collector = getItem(position)
        holder.bind(collector)

        holder.itemView.setOnClickListener { view ->
            val bundle = Bundle().apply {
                putInt("collectorId", collector.id)
            }

            view.findNavController().navigate(
                R.id.action_navigation_collectors_to_navigation_collector_detail,
                bundle
            )
        }
    }

    class CollectorViewHolder(private val binding: CollectorItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            binding.collector = collector
            binding.executePendingBindings()
        }
    }

    class CollectorDiffCallback : DiffUtil.ItemCallback<Collector>() {
        override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
            return oldItem == newItem
        }
    }
}
