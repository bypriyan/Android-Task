package com.socialseller.dummyapplication.repository

import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.bustrackingsystem.utility.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.socialseller.dummyapplication.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager,
    private val gson: Gson
) {

    suspend fun fetchUserDetails(): Result<User> = withContext(Dispatchers.IO) {
        try {
            val uid = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("User not logged in"))
            val snapshot = firestore.collection(Constants.KEY_USERS).document(uid).get().await()

            if (snapshot.exists()) {
                val user = snapshot.toObject(User::class.java) ?: throw Exception("User data is null")
                val userJson = gson.toJson(user)
                dataStoreManager.putString(Constants.KEY_USER_DATA, userJson)
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserFromDataStore(): User? = withContext(Dispatchers.IO) {
        val json = dataStoreManager.getString(Constants.KEY_USER_DATA).firstOrNull()
        json?.let { gson.fromJson(it, User::class.java) }
    }

    suspend fun saveUserToDataStore(user: User) {
        dataStoreManager.putString(Constants.KEY_USER_DATA, gson.toJson(user))
    }

}
