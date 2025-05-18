package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.android.volley.VolleyError
import com.example.viniloapp.models.Artist
import com.example.viniloapp.repositories.ArtistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ArtistViewModel(application: Application) : AndroidViewModel(application) {

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> = _artists

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val artistRepository = ArtistRepository(application)

    init {
        Log.d("ArtistViewModel", "ViewModel initialized")
        loadArtists()
    }

    fun loadArtists() {
        Log.d("ArtistViewModel", "Loading artists")
        _isLoading.value = true
        _error.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val artists = artistRepository.getArtists()
                _artists.postValue(artists)
            } catch (e: VolleyError) {
                Log.e("ArtistViewModel", "Error loading artists", e)
                _error.postValue("Error loading artists: ${e.message}")
            } catch (e: Exception) {
                Log.e("ArtistViewModel", "Error loading artists", e)
                _error.postValue("Unknown error loading artists")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                return ArtistViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 