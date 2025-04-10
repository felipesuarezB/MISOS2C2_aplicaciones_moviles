package com.example.viniloapp.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.viniloapp.databinding.FragmentArtistListBinding

class ArtistListFragment : Fragment() {
    private var _binding: FragmentArtistListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Aquí implementaremos la lógica para mostrar la lista de artistas
        binding.textView.text = "Lista de Artistas (Próximamente)"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 