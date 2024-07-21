package com.example.motorsportcalendar.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.motorsportcalendar.data.model.F1
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeViewModel : ViewModel() {private val _raceList = MutableLiveData<List<F1?>>()
    val raceList: LiveData<List<F1?>> = _raceList

    init {
        fetchRaceData(true) // Fetch initial data, you can pass a parameter for current/historical
    }

    private fun fetchRaceData(current: Boolean) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val direction = if (current) Query.Direction.ASCENDING else Query.Direction.DESCENDING
        db.collection("F1").orderBy("date", direction).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    val raceList = list.mapNotNull { it.toObject(F1::class.java) }
                    _raceList.value = raceList
                } else {
                    // Handle empty data scenario
                    _raceList.value = emptyList()
                }
            }
            .addOnFailureListener {
                // Handle error scenario
                _raceList.value = emptyList()
            }
    }
}