package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.android.volley.VolleyError
import com.example.viniloapp.models.Collector
import com.example.viniloapp.repositories.CollectorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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
    private val collectorRepository = CollectorRepository(application)

    init {
        Log.d("CollectorViewModel", "ViewModel inicializado")
        loadCollectors()
    }

    fun loadCollectors() {
        Log.d("CollectorViewModel", "Iniciando carga de coleccionistas")
        _isLoading.value = true
        _error.value = ""

        viewModelScope.launch {
            try {
                val collector = collectorRepository.getCollectors()
                _collectors.value = collector
            } catch (e: VolleyError) {
                Log.e("CollectorViewModel", "VolleyError getting collectors: $e.toString()")
                _error.value = e.message.toString()
            } catch (e: Exception) {
                Log.e("CollectorViewModel", "Error getting collectors: $e.toString()")
                _error.value = "Error desconocido al cargar los coleccionistas"
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
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                return CollectorViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}