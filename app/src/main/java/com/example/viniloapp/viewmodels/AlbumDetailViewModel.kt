package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viniloapp.models.AlbumDetail
import com.example.viniloapp.network.AlbumService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _albumDetail = MutableLiveData<AlbumDetail>()
    val albumDetail: LiveData<AlbumDetail> = _albumDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Log.d("AlbumDetailViewModel", "Initializing view model")
    }

    fun loadAlbumDetail(albumId: Int) {
        _isLoading.value = true
        _error.value = ""

        viewModelScope.launch {
            val albumService = AlbumService()
            albumService.getAlbumDetail(
                albumId,
                { album ->
                    _albumDetail.value = album
                    _isLoading.value = false
                },
                { e ->
                    Log.e("AlbumDetailViewModel", "Error loading detail", e)
                    _error.value = "Error al cargar el álbum: ${e.message}"
                    _isLoading.value = false

                }
            )
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                return AlbumDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
} 