package com.example.motorsportcalendar.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.motorsportcalendar.data.model.F1// Import your F1 data class

class SharedViewModel : ViewModel() {
    private val _selectedFilterCategory = MutableLiveData("All") // Initial value
    val selectedFilterCategory: LiveData<String> = _selectedFilterCategory

    fun updateFilterCategory(category: String) {
        _selectedFilterCategory.value = category
    }

    private val _selectedEvent = MutableStateFlow<F1?>(null)
    val selectedEvent: StateFlow<F1?> = _selectedEvent.asStateFlow()

    fun setSelectedEvent(event: F1) {
        _selectedEvent.value = event
    }
}