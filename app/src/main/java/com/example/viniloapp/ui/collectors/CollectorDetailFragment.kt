package com.example.viniloapp.ui.collectors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.databinding.FragmentCollectorDetailBinding
import com.example.viniloapp.viewmodels.CollectorDetailViewModel


class CollectorDetailFragment: Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CollectorDetailFragment", "Creating Collector Detail view")
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return  binding.root

    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CollectorDetailViewModel::class.java)
        viewModel.collectorDetail.observe(viewLifecycleOwner, Observer {
            collectorDetail ->
            collectorDetail?.let {
                binding.collectorDetail = collectorDetail
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
