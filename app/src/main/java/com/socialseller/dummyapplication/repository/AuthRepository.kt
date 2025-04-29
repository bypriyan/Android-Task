package com.socialseller.dummyapplication.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.ui.unit.Constraints
import com.bypriyan.bustrackingsystem.utility.Constants
import com.bypriyan.togocartstore.DI.module.AppModule
import com.bypriyan.togocartuser.DI.DaggerHiltClass
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    suspend fun login(email: String, password: String): Result<FirebaseUser?> = withContext(Dispatchers.IO) {
        try {
            val result = Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password))
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun registerUser(
        email: String,
        password: String,
        fullName: String,
        imageUri: Uri
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID not found")

            val compressedImage = compressImage(imageUri)

            val storageRef = storage.reference.child("profileImages/$uid.jpg")
            storageRef.putBytes(compressedImage).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            Log.d("signup", "Image uploaded: $downloadUrl")

            val userMap = mapOf(
                "uid" to uid,
                "email" to email,
                "name" to fullName,
                "profileImageUrl" to downloadUrl
            )

            firestore.collection(Constants.KEY_USERS).document(uid)
                .set(userMap)
                .addOnSuccessListener {
                    Log.d("signup", "User added to Firestore successfully")
                }
                .addOnFailureListener {
                    Log.e("signup", "Error adding user to Firestore", it)
                }
                .await() // <-- important to await this if inside coroutine

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("signup", "Registration failed", e)
            Result.failure(e)
        }
    }

    private suspend fun compressImage(imageUri: Uri): ByteArray = withContext(Dispatchers.IO) {
        val inputStream = DaggerHiltClass.context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        stream.toByteArray()
    }

}
