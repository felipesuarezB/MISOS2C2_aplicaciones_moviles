package com.example.viniloapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viniloapp.models.CollectorDetail

class CollectorDetailViewModel(application: Application): AndroidViewModel(application) {
    private val _collectorDetail = MutableLiveData<CollectorDetail>()
    val collectorDetail: LiveData<CollectorDetail> = _collectorDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        Log.d("CollectorDetailViewModel", "Initializing view model")
        // TODO: Add logic to call  http://apihost/collector/:collectorId and populates _collectorDetail
        _collectorDetail.value = CollectorDetail(1, "User Name", "30002", "test@test.com")
    }

    class Factory(private val application: Application): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                return CollectorDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel Class")
        }
    }

}
