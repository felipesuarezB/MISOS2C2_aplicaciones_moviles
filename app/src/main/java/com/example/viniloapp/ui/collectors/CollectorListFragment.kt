package com.example.viniloapp.ui.collectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.viniloapp.databinding.FragmentCollectorListBinding

class CollectorListFragment : Fragment() {
    private var _binding: FragmentCollectorListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Aquí implementaremos la lógica para mostrar la lista de coleccionistas
        binding.textView.text = "Lista de Coleccionistas (Próximamente)"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 