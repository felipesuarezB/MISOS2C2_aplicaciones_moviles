package com.example.viniloapp.ui.prizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.viniloapp.R
import com.example.viniloapp.viewmodels.PrizeViewModel

class PrizeCreateFragment : Fragment() {
    private val prizeViewModel: PrizeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prize_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText = view.findViewById<EditText>(R.id.editTextPrizeName)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextPrizeDescription)
        val organizationEditText = view.findViewById<EditText>(R.id.editTextPrizeOrganization)
        val createButton = view.findViewById<Button>(R.id.buttonCreatePrize)

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val organization = organizationEditText.text.toString()

            if (name.isBlank() || description.isBlank() || organization.isBlank()) {
                Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                prizeViewModel.createPrize(name, description, organization)
            }
        }

        prizeViewModel.creationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                prizeViewModel.clearCreationResult()
            }
        }
        prizeViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                prizeViewModel.clearCreationResult()
            }
        }
    }
}
