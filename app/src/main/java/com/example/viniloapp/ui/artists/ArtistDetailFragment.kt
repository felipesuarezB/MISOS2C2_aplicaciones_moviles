package com.example.viniloapp.ui.artists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viniloapp.databinding.FragmentArtistDetailBinding
import com.example.viniloapp.ui.adapters.ArtistAlbumsAdapter
import com.example.viniloapp.ui.adapters.PerformerPrizesAdapter
import com.example.viniloapp.viewmodels.ArtistDetailViewModel

class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ArtistDetailViewModel
    private lateinit var albumsAdapter: ArtistAlbumsAdapter
    private lateinit var prizesAdapter: PerformerPrizesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ArtistDetailFragment", "Creating Artist Detail view")
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ArtistDetailViewModel.Factory(requireActivity().application)
        )[ArtistDetailViewModel::class.java]

        // Configure ActionBar for detail view
        configureActionBar(true)

        binding.progressBar.visibility = View.VISIBLE

        val artistId = arguments?.getInt("artistId") ?: return

        // Set up albums RecyclerView
        val albumsRecyclerView = binding.albumsRecyclerView
        albumsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        albumsAdapter = ArtistAlbumsAdapter()
        albumsRecyclerView.adapter = albumsAdapter

        // Set up prizes RecyclerView
        val prizesRecyclerView = binding.prizesRecyclerView
        prizesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        prizesAdapter = PerformerPrizesAdapter()
        prizesRecyclerView.adapter = prizesAdapter

        // Log RecyclerView dimensions
        albumsRecyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.d("ArtistDetailFragment", "Albums RecyclerView dimensions: width=${right-left}, height=${bottom-top}")
        }

        viewModel.loadArtistDetail(artistId)

        viewModel.artistDetail.observe(viewLifecycleOwner, Observer { artist ->
            artist?.let {
                binding.artist = artist
                binding.progressBar.visibility = View.GONE
                
                Log.d("ArtistDetailFragment", "Artist ${artist.name} loaded successfully")
                Log.d("ArtistDetailFragment", "Albums: ${artist.albums.size}, Prizes: ${artist.performerPrizes.size}")
                
                // Update albums RecyclerView
                albumsAdapter.submitList(artist.albums)
                
                // Update prizes RecyclerView
                prizesAdapter.submitList(artist.performerPrizes)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
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