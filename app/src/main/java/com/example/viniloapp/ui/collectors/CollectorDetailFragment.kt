package com.example.viniloapp.ui.collectors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.viniloapp.databinding.FragmentCollectorDetailBinding


class CollectorDetailFragment: Fragment() {
    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelAdapter: CollectorDetailAdapter()
    private var collectorId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectorId = arguments?.getInt("collectorId") ?: collectorId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return  binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
