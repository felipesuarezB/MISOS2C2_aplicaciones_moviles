package com.example.viniloapp.ui.albums

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
import com.example.viniloapp.databinding.FragmentAlbumDetailBinding
import com.example.viniloapp.ui.adapters.TrackAdapter
import com.example.viniloapp.viewmodels.AlbumDetailViewModel

class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("AlbumDetailFragment", "Creating Album Detail view")
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AlbumDetailViewModel::class.java)

        // Configure ActionBar for detail view
        configureActionBar(true)

        binding.progressBar.visibility = View.VISIBLE

        val albumId = arguments?.getInt("albumId") ?: return

        val tracksRecyclerView = binding.tracksRecyclerView
        tracksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        trackAdapter = TrackAdapter()
        tracksRecyclerView.adapter = trackAdapter

        // Add RecyclerView state logging
        tracksRecyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.d("AlbumDetailFragment", "RecyclerView dimensions: width=${right-left}, height=${bottom-top}")
        }

        viewModel.loadAlbumDetail(albumId)

        viewModel.albumDetail.observe(viewLifecycleOwner, Observer { albumDetail ->
            albumDetail?.let {
                binding.albumDetail = albumDetail
                binding.progressBar.visibility = View.GONE
                Log.d("AlbumDetailFragment", "Total tracks to display: ${albumDetail.tracks.size}")
                albumDetail.tracks.forEach { track ->
                    Log.d("AlbumDetailFragment", "Track: ${track.name}")
                }
                trackAdapter.submitList(albumDetail.tracks)
                
                // Log RecyclerView state after data is loaded
                Log.d("AlbumDetailFragment", "RecyclerView visibility: ${tracksRecyclerView.visibility}")
                Log.d("AlbumDetailFragment", "RecyclerView height: ${tracksRecyclerView.height}")
                Log.d("AlbumDetailFragment", "RecyclerView measured height: ${tracksRecyclerView.measuredHeight}")
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

    private fun configureActionBar(showBackButton: Boolean) {
        (requireActivity() as androidx.appcompat.app.AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(showBackButton)
            setDisplayShowHomeEnabled(showBackButton)
        }
    }
} 