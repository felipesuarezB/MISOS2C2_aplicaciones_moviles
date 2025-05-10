package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.VolleyError
import com.example.viniloapp.models.CollectorDetail
import com.example.viniloapp.repositories.CollectorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CollectorDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _collectorDetail = MutableLiveData<CollectorDetail>()
    val collectorDetail: LiveData<CollectorDetail> = _collectorDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val collectorRepository = CollectorRepository(application)

    init {
        Log.d("CollectorDetailViewModel", "Initializing view model")
    }

    fun loadCollectorDetail(collectorId: Int) {
        _isLoading.value = true
        _error.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val collector = collectorRepository.getCollectorDetail(collectorId)
                _collectorDetail.postValue(collector)
            } catch (e: VolleyError) {
                Log.e("CollectorDetailViewModel", e.toString())
                _error.postValue("Error desconocido al cargar el coleccionista ${e.toString()}")

            } catch (e: Exception) {
                Log.e("CollectorDetailViewModel", e.toString())
                _error.postValue("Error desconocido al cargar el coleccionista")
            } finally {
                _isLoading.postValue(false)
            }

        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
                return CollectorDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }
}