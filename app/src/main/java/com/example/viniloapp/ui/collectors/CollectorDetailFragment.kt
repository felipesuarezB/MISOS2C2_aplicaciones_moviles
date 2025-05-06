package com.example.viniloapp.ui.collectors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.databinding.FragmentCollectorDetailBinding
import com.example.viniloapp.ui.adapters.CollectorAlbumAdapter
import com.example.viniloapp.viewmodels.CollectorDetailViewModel
import com.example.viniloapp.ui.adapters.CommentAdapter

class CollectorDetailFragment: Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CollectorDetailViewModel
    private lateinit var collectorAlbumAdapter: CollectorAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CollectorDetailFragment", "Creating Collector Detail view")
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CollectorDetailViewModel::class.java)

        // Show back button in detail view
        (requireActivity() as androidx.appcompat.app.AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.progressBar.visibility = View.VISIBLE

        val collectorId = arguments?.getInt("collectorId") ?: return

        val albumsRecyclerView = binding.albumsRecyclerView
        albumsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        collectorAlbumAdapter = CollectorAlbumAdapter()
        albumsRecyclerView.adapter = collectorAlbumAdapter

        viewModel.loadCollectorDetail(collectorId)

        viewModel.collectorDetail.observe(viewLifecycleOwner, Observer { collectorDetail ->
            collectorDetail?.let {
                binding.collectorDetail = collectorDetail
                binding.progressBar.visibility = View.GONE

                collectorAlbumAdapter.submitList(collectorDetail.collectorAlbums)
                // Configurar RecyclerView para comentarios
                val commentsRecyclerView = binding.commentsRecyclerView
                commentsRecyclerView.setHasFixedSize(true)
                commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                commentsRecyclerView.adapter = CommentAdapter(collectorDetail.comments)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
