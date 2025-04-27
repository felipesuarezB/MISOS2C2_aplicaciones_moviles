package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.viniloapp.models.Collector
import com.example.viniloapp.network.CollectorService
import com.example.viniloapp.network.NetworkServiceAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectorViewModel(application: Application) : AndroidViewModel(application) {

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>> = _collectors

    private val _currentCollector = MutableLiveData<Collector>()
    val currentCollector: LiveData<Collector> = _currentCollector

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        Log.d("CollectorViewModel", "ViewModel inicializado")
        loadCollectors()
    }

    fun loadCollectors() {
        Log.d("CollectorViewModel", "Iniciando carga de coleccionistas")
        _isLoading.value = true
        _error.value = ""

        NetworkServiceAdapter.getInstance(getApplication()).getCollectors(
            onComplete = { response ->
                Log.d("CollectorViewModel", "Respuesta recibida: ${response.size} coleccionistas")
                response.forEach { collector ->
                    Log.d("CollectorViewModel", "Name: ${collector.name}, Telephone: ${collector.telephone}, Email: ${collector.email}")
                }
                _collectors.value = response
                _isLoading.value = false
            },
            onError = { error ->
                Log.e("CollectorViewModel", "Error al cargar coleccionistas", error)
                _error.value = "Error al cargar los coleccionistas: ${error.message}"
                _isLoading.value = false
            }
        )
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                return CollectorViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}