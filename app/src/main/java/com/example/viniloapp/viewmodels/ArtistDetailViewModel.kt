package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.VolleyError
import com.example.viniloapp.models.Artist
import com.example.viniloapp.repositories.ArtistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ArtistDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _artistDetail = MutableLiveData<Artist>()
    val artistDetail: LiveData<Artist> = _artistDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val artistRepository = ArtistRepository(application)

    init {
        Log.d("ArtistDetailViewModel", "Initializing view model")
    }

    fun loadArtistDetail(artistId: Int) {
        _isLoading.value = true
        _error.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val artistDetail = artistRepository.getArtistDetail(artistId)
                _artistDetail.postValue(artistDetail)
            } catch (e: VolleyError) {
                Log.e("ArtistDetailViewModel", "Error loading artist detail", e)
                _error.postValue("Error al cargar el artista: ${e.message}")
            } catch (e: Exception) {
                Log.e("ArtistDetailViewModel", "Error loading artist detail", e)
                _error.postValue("Error desconocido al cargar el artista")
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
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java)) {
                return ArtistDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
} 