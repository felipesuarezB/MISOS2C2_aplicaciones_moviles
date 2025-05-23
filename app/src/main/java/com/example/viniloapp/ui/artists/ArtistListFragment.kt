package com.example.viniloapp.ui.artists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viniloapp.R
import com.example.viniloapp.ui.adapters.ArtistsAdapter
import com.example.viniloapp.viewmodels.ArtistViewModel

class ArtistListFragment : Fragment() {

    private lateinit var viewModel: ArtistViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArtistsAdapter
    private lateinit var progressBar: View
    private lateinit var buttonCreatePrize: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Hide back button in main view
        (requireActivity() as androidx.appcompat.app.AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        recyclerView = view.findViewById(R.id.artists_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        buttonCreatePrize = view.findViewById(R.id.button_create_prize)
        buttonCreatePrize.setOnClickListener {
            findNavController().navigate(R.id.action_artistListFragment_to_prizeCreateFragment)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ArtistsAdapter()
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ArtistViewModel.Factory(requireActivity().application)
        )[ArtistViewModel::class.java]

        viewModel.artists.observe(viewLifecycleOwner) { artists ->
            Log.d("ArtistListFragment", "Artists updated: ${artists.size} artists")
            adapter.submitList(artists)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("ArtistListFragment", "Loading state: $isLoading")
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Log.e("ArtistListFragment", "Error received: $error")
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }

        buttonCreatePrize.setOnClickListener {
            findNavController().navigate(R.id.action_artistListFragment_to_prizeCreateFragment)
        }

        Log.d("ArtistListFragment", "Loading artists")
        viewModel.loadArtists()
    }

}