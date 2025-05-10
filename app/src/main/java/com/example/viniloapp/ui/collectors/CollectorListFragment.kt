package com.example.viniloapp.ui.collectors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.R
import com.example.viniloapp.ui.adapters.CollectorsAdapter
import com.example.viniloapp.viewmodels.CollectorViewModel

class CollectorListFragment : Fragment() {

    private lateinit var viewModel: CollectorViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CollectorsAdapter
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collector_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        (requireActivity() as androidx.appcompat.app.AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        recyclerView = view.findViewById(R.id.collectors_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CollectorsAdapter()
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            CollectorViewModel.Factory(requireActivity().application)
        )[CollectorViewModel::class.java]

        viewModel.collectors.observe(viewLifecycleOwner) { collectors ->
            Log.d("CollectorListFragment", "Coleccionistas actualizados: ${collectors.size} coleccionistas")
            adapter.submitList(collectors)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("CollectorListFragment", "Estado de carga: $isLoading")
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Log.e("CollectorListFragment", "Error recibido: $error")
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }

        Log.d("CollectorListFragment", "Iniciando carga de coleccionistas")
        viewModel.loadCollectors()
    }
} 