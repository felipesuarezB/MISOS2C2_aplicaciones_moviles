package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viniloapp.models.CollectorDetail
import com.example.viniloapp.network.CollectorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectorDetailViewModel(application: Application): AndroidViewModel(application) {
    private val _collectorDetail = MutableLiveData<CollectorDetail>()
    val collectorDetail: LiveData<CollectorDetail> = _collectorDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val collectorService = CollectorService()

    init {
        Log.d("CollectorDetailViewModel", "Initializing view model")
    }

    fun loadCollectorDetail(collectorId: Int) {
        _isLoading.value = true
        _error.value = ""

        collectorService.getCollectorDetail(
            collectorId,
            { collector ->
                _collectorDetail.value = collector
                _isLoading.value = false
            },
            {
                e ->
                Log.e("CollectorDetailViewModel", "Error loading detail", e)
                _error.value = "Error al cargar el coleccionista: ${e.message}"
                _isLoading.value = false

            }
        )
    }

    class Factory(private val application: Application): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
                return CollectorDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}