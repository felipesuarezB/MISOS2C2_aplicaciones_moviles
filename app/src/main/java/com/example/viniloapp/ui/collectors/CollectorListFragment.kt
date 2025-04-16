package com.example.viniloapp.ui.collectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.databinding.FragmentCollectorListBinding
import com.example.viniloapp.ui.adapters.CollectorsAdapter
import com.example.viniloapp.viewmodels.CollectorViewModel

class CollectorListFragment : Fragment() {
    private var _binding:FragmentCollectorListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelAdapter: CollectorsAdapter
    private lateinit var viewModel: CollectorViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorListBinding.inflate(inflater, container, false)
        viewModelAdapter = CollectorsAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.collectorsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        // Setup the ViewModel
        viewModel = ViewModelProvider(this).get(CollectorViewModel::class.java)

        // Observe the LiveData from the ViewModel and update the RecyclerView adapter
        viewModel.collectors.observe(viewLifecycleOwner, Observer { collectors ->
            collectors?.let {
                viewModelAdapter.submitList(it) // Submit list to the adapter
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            isLoading ->
                binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.INVISIBLE
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCollectors()
    }

}