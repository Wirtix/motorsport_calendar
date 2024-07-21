package com.example.motorsportcalendar.screens

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val _notifyF1 = MutableStateFlow(false) // Get initial value from SharedPreferences
    val notifyF1: StateFlow<Boolean> = _notifyF1.asStateFlow()

    private val _notifyWEC = MutableStateFlow(false) // Get initial value from SharedPreferences
    val notifyWEC: StateFlow<Boolean> = _notifyWEC.asStateFlow()

    private val _notifyWRC = MutableStateFlow(false) // Get initial value from SharedPreferences
    val notifyWRC: StateFlow<Boolean> = _notifyWRC.asStateFlow()

    private val _notifyMotoGP = MutableStateFlow(false) // Get initial value from SharedPreferences
    val notifyMotoGP: StateFlow<Boolean> = _notifyMotoGP.asStateFlow()

    private val _notifyNASCAR = MutableStateFlow(false) // Get initial value from SharedPreferences
    val notifyNASCAR: StateFlow<Boolean> = _notifyNASCAR.asStateFlow()


    init {
        loadNotificationPreferences()
    }

    fun setNotifyF1(enabled: Boolean) {
        _notifyF1.value = enabled
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notify_f1", enabled).apply()
        // Save to SharedPreferences and send to server
    }
    fun setNotifyWEC(enabled: Boolean) {
        _notifyWEC.value = enabled
        val sharedPreferences= context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notify_wec", enabled).apply()
        // Save to SharedPreferences and send to server
    }
    fun setNotifyWRC(enabled: Boolean) {
        _notifyWRC.value = enabled
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notify_wrc", enabled).apply()
        // Save to SharedPreferences and send to server
    }
    fun setNotifyMotoGP(enabled: Boolean) {
        _notifyMotoGP.value = enabled
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notify_motogp", enabled).apply()
        // Save to SharedPreferences and send to server
    }
    fun setNotifyNASCAR(enabled: Boolean) {
        _notifyNASCAR.value = enabled
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notify_nascar", enabled).apply()
        // Save to SharedPreferences and send to server
    }


    private fun loadNotificationPreferences() {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        _notifyF1.value = sharedPreferences.getBoolean("notify_f1", false) // Default to false
        _notifyWEC.value = sharedPreferences.getBoolean("notify_wec", false)
        _notifyWRC.value = sharedPreferences.getBoolean("notify_wrc", false)
        _notifyMotoGP.value = sharedPreferences.getBoolean("notify_motogp", false)
        _notifyNASCAR.value = sharedPreferences.getBoolean("notify_nascar", false)
    }

    // ... similar functions for setNotifyWEC and setNotifyWRC
}