package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.volley.VolleyError
import com.example.viniloapp.models.Album
import com.example.viniloapp.network.AlbumService
import com.example.viniloapp.network.NetworkServiceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> = _albums

    private val _currentAlbum = MutableLiveData<Album>()
    val currentAlbum: LiveData<Album> = _currentAlbum

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    private val _creationResult = MutableLiveData<String?>()
    val creationResult: LiveData<String?> = _creationResult

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val albumService = AlbumService()

    init {
        Log.d("AlbumViewModel", "ViewModel inicializado")
        loadAlbums()
    }

    fun loadAlbums() {
        Log.d("AlbumViewModel", "Iniciando carga de álbumes")
        _isLoading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val albums = albumService.getAlbums()
                _albums.value = albums
            } catch (e: VolleyError) {
                Log.e("AlbumViewModel", "Error al cargar álbumes", e)
                _error.value = "Error al cargar los álbumes: ${e.message}"
            } catch (e: Exception) {
                Log.e("AlbumViewModel", "Error al cargar álbumes", e)
                _error.value = "Error desconocido al cargar los álbumes"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createAlbum(
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String
    ) {

        val jsonBody: JSONObject = JSONObject().apply {
            put("name", name)
            put("cover", cover)
            put("releaseDate", releaseDate)
            put("description", description)
            put("genre", genre)
            put("recordLabel", recordLabel)
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                albumService.createAlbum(jsonBody)
                _creationResult.postValue("Álbum creado exitosamente")            } catch (error: VolleyError) {
                val responseData = error.networkResponse.data
                val jsonResponse = JSONObject(String(responseData, Charsets.UTF_8))
                val stringError = jsonResponse.optString("message", "unknown error")
                Log.d("Network-CreateAlbum", stringError)
                _error.value = stringError
            } catch (error: Exception) {
                Log.d("Network-CreateAlbum", error.toString())
                _error.value = "Error desconocido al crear el album"
            } finally {
                _isLoading.value = false
            }
        }

    }

    fun clearCreationResult() {
        _creationResult.value = null
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