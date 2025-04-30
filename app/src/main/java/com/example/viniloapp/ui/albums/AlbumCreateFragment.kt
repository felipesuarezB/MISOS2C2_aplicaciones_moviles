package com.example.viniloapp.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.viniloapp.R
import com.example.viniloapp.databinding.FragmentAlbumCreateBinding
import com.example.viniloapp.viewmodels.AlbumViewModel

class AlbumCreateFragment : Fragment() {
    private var _binding: FragmentAlbumCreateBinding? = null
    private val binding get() = _binding!!
    private val albumViewModel: AlbumViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCreateAlbum.setOnClickListener {
            val name = binding.editTextAlbumName.text.toString()
            val cover = binding.editTextAlbumCover.text.toString()
            val releaseDate = binding.editTextAlbumReleaseDate.text.toString()
            val description = binding.editTextAlbumDescription.text.toString()
            val genre = binding.editTextAlbumGenre.text.toString()
            val recordLabel = binding.editTextAlbumRecordLabel.text.toString()

            albumViewModel.createAlbum(
                name,
                cover,
                releaseDate,
                description,
                genre,
                recordLabel
            )
        }

        albumViewModel.creationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it) {
                    Toast.makeText(requireContext(), "Álbum creado exitosamente", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Error al crear álbum", Toast.LENGTH_SHORT).show()
                }
                albumViewModel.clearCreationResult()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
