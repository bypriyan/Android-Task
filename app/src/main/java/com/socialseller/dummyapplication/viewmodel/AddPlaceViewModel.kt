package com.socialseller.dummyapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.maps.model.LatLng
import com.socialseller.dummyapplication.repository.PlaceRepository
import com.socialseller.dummyapplication.room.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPlaceViewModel @Inject constructor(
    private val repository: PlaceRepository
) : ViewModel() {

    private val _result = MutableSharedFlow<Result<Unit>>()
    val result = _result.asSharedFlow()

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    fun uploadPlace(uri: Uri, name: String, desc: String, latLng: LatLng) {
        viewModelScope.launch {
            val res = repository.uploadImageAndSavePlace(uri, name, desc, latLng)
            _result.emit(res)
            // Refresh list after upload
            loadPlaces()
        }
    }

    fun syncFromFirestore() {
        viewModelScope.launch {
            repository.syncPlacesFromFirestore()
            loadPlaces()
        }
    }

    fun loadPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getLocalPlaces() // You'll define this next
            _places.postValue(data)
        }
    }
}

