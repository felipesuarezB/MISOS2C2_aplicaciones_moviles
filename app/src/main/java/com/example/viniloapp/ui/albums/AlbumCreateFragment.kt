package com.example.viniloapp.ui.albums

import android.app.DatePickerDialog
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
import java.util.Calendar
import java.util.Locale

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

        binding.editTextAlbumReleaseDate.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        val formattedDate = String.format(
                            Locale.US, // or Locale.getDefault() if you're okay with device locale
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDayOfMonth
                        )
                        setText(formattedDate)
                    },
                    year, month, day
                )
                datePickerDialog.show()
            }
        }

        binding.buttonCreateAlbum.setOnClickListener {
            val name = binding.editTextAlbumName.text.toString()
            val cover = binding.editTextAlbumCover.text.toString()
            val releaseDate = binding.editTextAlbumReleaseDate.text.toString()
            val description = binding.editTextAlbumDescription.text.toString()
            val genre = binding.editTextAlbumGenre.text.toString()
            val recordLabel = binding.editTextAlbumRecordLabel.text.toString()


            var hasError = false

            if (name.isBlank()) {
                binding.editTextAlbumName.error = "El nombre no puede estar vacío"
                hasError = true
            }
            if (cover.isNotBlank() && !android.util.Patterns.WEB_URL.matcher(cover).matches()) {
                binding.editTextAlbumCover.error = "URL no válida"
                hasError = true
            }
            if (!releaseDate.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                binding.editTextAlbumReleaseDate.error = "Fecha inválida (formato: YYYY-MM-DD)"
                hasError = true
            }
            if (description.isBlank()) {
                binding.editTextAlbumDescription.error = "Descripción requerida"
                hasError = true
            }
            if (genre.isBlank()) {
                binding.editTextAlbumGenre.error = "Género requerido"
                hasError = true
            }
            if (recordLabel.isBlank()) {
                binding.editTextAlbumRecordLabel.error = "Sello discográfico requerido"
                hasError = true
            }

            if (!hasError) {
                albumViewModel.createAlbum(name, cover, releaseDate, description, genre, recordLabel)
            }

        }

        albumViewModel.error.observe(viewLifecycleOwner) { result ->
            result?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                albumViewModel.clearCreationResult()
            }
        }

        albumViewModel.creationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
                albumViewModel.clearCreationResult()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
