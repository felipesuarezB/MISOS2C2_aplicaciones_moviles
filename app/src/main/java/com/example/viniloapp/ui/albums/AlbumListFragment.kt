package com.example.viniloapp.ui.albums

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
import com.example.viniloapp.ui.adapters.AlbumsAdapter
import com.example.viniloapp.viewmodels.AlbumViewModel

class AlbumListFragment : Fragment() {

    private lateinit var viewModel: AlbumViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlbumsAdapter
    private lateinit var progressBar: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AlbumListFragment", "onViewCreated iniciado")

        recyclerView = view.findViewById(R.id.albums_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AlbumsAdapter()
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(requireActivity().application)
        )[AlbumViewModel::class.java]

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            Log.d("AlbumListFragment", "Albums actualizados: ${albums.size} álbumes")
            adapter.submitList(albums)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("AlbumListFragment", "Estado de carga: $isLoading")
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Log.e("AlbumListFragment", "Error recibido: $error")
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }

        val addButton = view.findViewById<Button>(R.id.button_add_album)
        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AlbumCreateFragment())
                .addToBackStack(null)
                .commit()
        }

        Log.d("AlbumListFragment", "Iniciando carga de álbumes")
        viewModel.loadAlbums()
    }
} 