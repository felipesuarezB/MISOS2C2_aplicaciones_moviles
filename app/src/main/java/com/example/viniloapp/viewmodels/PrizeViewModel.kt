package com.example.viniloapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.viniloapp.repositories.PrizeRepository
import kotlinx.coroutines.launch

class PrizeViewModel(application: Application) : AndroidViewModel(application) {
    private val prizeRepository = PrizeRepository(application)

    private val _creationResult = MutableLiveData<String?>()
    val creationResult: LiveData<String?> = _creationResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun createPrize(name: String, description: String, organization: String) {
        viewModelScope.launch {
            try {
                prizeRepository.createPrize(name, description, organization)
                _creationResult.value = "Premio creado exitosamente"
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido al crear el premio"
            }
        }
    }

    fun clearCreationResult() {
        _creationResult.value = null
        _error.value = null
    }
}
