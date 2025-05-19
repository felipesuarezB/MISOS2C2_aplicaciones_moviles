package com.example.viniloapp.ui.artists

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
import com.example.viniloapp.ui.adapters.ArtistsAdapter
import com.example.viniloapp.viewmodels.ArtistViewModel

class ArtistListFragment : Fragment() {

    private lateinit var viewModel: ArtistViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArtistsAdapter
    private lateinit var progressBar: View
    private lateinit var fabCreatePrize: View

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
        fabCreatePrize = view.findViewById(R.id.fab_create_prize)

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

        fabCreatePrize.setOnClickListener {
            showCreatePrizeDialog()
        }

        Log.d("ArtistListFragment", "Loading artists")
        viewModel.loadArtists()
    }

    private fun showCreatePrizeDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_prize, null)
        val imageUrlEditText = dialogView.findViewById<android.widget.EditText>(R.id.editTextPrizeImageUrl)
        val nameEditText = dialogView.findViewById<android.widget.EditText>(R.id.editTextPrizeName)
        val descriptionEditText = dialogView.findViewById<android.widget.EditText>(R.id.editTextPrizeDescription)
        val organizationEditText = dialogView.findViewById<android.widget.EditText>(R.id.editTextPrizeOrganization)
        val createButton = dialogView.findViewById<android.widget.Button>(R.id.buttonCreatePrize)

        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val organization = organizationEditText.text.toString()

            if (name.isBlank() || description.isBlank() || organization.isBlank()) {
                Toast.makeText(requireContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar al backend para crear el premio
            createPrizeOnBackend(name, description, organization, alertDialog)
        }

        alertDialog.show()
    }

    private fun createPrizeOnBackend(name: String, description: String, organization: String, alertDialog: androidx.appcompat.app.AlertDialog) {
        // Usar corrutinas para llamada de red
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.Main) {
            try {
                val jsonBody = org.json.JSONObject()
                jsonBody.put("name", name)
                jsonBody.put("description", description)
                jsonBody.put("organization", organization)

                val response = com.example.viniloapp.network.NetworkServiceAdapter.getInstance(requireContext())
                    .post("prizes", jsonBody)
                Toast.makeText(requireContext(), "Premio creado exitosamente", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al crear el premio: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}