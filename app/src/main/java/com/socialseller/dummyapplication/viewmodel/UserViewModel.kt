package com.socialseller.dummyapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.socialseller.dummyapplication.model.User
import com.socialseller.dummyapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    init {
        loadUserFromDataStore()      // Load cached data
        fetchUserFromServer()       // Sync fresh data
    }

    private fun loadUserFromDataStore() {
        viewModelScope.launch {
            val storedUser = userRepository.getUserFromDataStore()
            _user.value = storedUser
        }
    }

    private fun fetchUserFromServer() {
        viewModelScope.launch {
            val result = userRepository.fetchUserDetails()
            if (result.isSuccess) {
                val freshUser = result.getOrNull()
                freshUser?.let {
                    _user.value = it
                    userRepository.saveUserToDataStore(it) // Save latest to DataStore
                }
            } else {
                _error.emit(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            dataStoreManager.clear()
            firebaseAuth.signOut()
        }
    }

}
