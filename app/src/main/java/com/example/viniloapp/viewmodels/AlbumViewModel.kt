package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.viniloapp.models.Album
import com.example.viniloapp.network.AlbumService
import com.example.viniloapp.network.NetworkServiceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    private val _currentAlbum = MutableLiveData<Album>()
    val currentAlbum: LiveData<Album> = _currentAlbum

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Log.d("AlbumViewModel", "ViewModel inicializado")
        loadAlbums()
    }

    fun loadAlbums() {
        Log.d("AlbumViewModel", "Iniciando carga de álbumes")
        _isLoading.value = true
        _error.value = ""
        
        NetworkServiceAdapter.getInstance(getApplication()).getAlbums(
            onComplete = { response ->
                Log.d("AlbumViewModel", "Respuesta recibida: ${response.size} álbumes")
                response.forEach { album ->
                    Log.d("AlbumViewModel", "Álbum: ${album.name}, Género: ${album.genre}")
                }
                _albums.value = response
                _isLoading.value = false
            },
            onError = { error ->
                Log.e("AlbumViewModel", "Error al cargar álbumes", error)
                _error.value = "Error al cargar los álbumes: ${error.message}"
                _isLoading.value = false
            }
        )
    }

    fun loadAlbumDetails(albumId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val albumService = AlbumService()
                val albumsList = withContext(Dispatchers.IO) {
                    albumService.getAlbums()
                }
                val album = albumsList.find { it.id == albumId }
                if (album != null) {
                    _currentAlbum.value = album
                } else {
                    _error.value = "Álbum no encontrado"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
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
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                return AlbumViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}