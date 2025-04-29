package com.socialseller.dummyapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.socialseller.dummyapplication.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableSharedFlow<Result<FirebaseUser?>>()
    val loginResult: SharedFlow<Result<FirebaseUser?>> = _loginResult

    private val _registerState = MutableSharedFlow<Result<Unit>>()
    val registerState = _registerState.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.emit(Result.runCatching {
                repository.login(email, password).getOrThrow()
            })
        }
    }

    fun register(email: String, password: String, name: String, imageUri: Uri) {
        viewModelScope.launch {
            val result = repository.registerUser(email, password, name, imageUri)
            _registerState.emit(result)
        }
    }
}
