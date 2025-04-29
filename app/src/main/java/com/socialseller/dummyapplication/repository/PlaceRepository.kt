package com.socialseller.dummyapplication.repository

import android.net.Uri
import android.util.Log
import androidx.compose.ui.unit.Constraints
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bypriyan.bustrackingsystem.utility.Constants
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.socialseller.dummyapplication.room.AppDatabase
import com.socialseller.dummyapplication.room.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepository @Inject constructor(
    private val db: AppDatabase,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {


    suspend fun uploadImageAndSavePlace(
        uri: Uri,
        name: String,
        description: String,
        latLng: LatLng
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val imageRef = storage.reference.child("places/${UUID.randomUUID()}")
            imageRef.putFile(uri).await()
            val downloadUrl = imageRef.downloadUrl.await().toString()
            val generatedId = UUID.randomUUID().toString()
            val place = Place(
                id = generatedId,
                name = name,
                description = description,
                imageUrl = downloadUrl,
                latitude = latLng.latitude,
                longitude = latLng.longitude
            )
            firestore.collection("places").document(generatedId).set(place).await()
            db.placeDao().insertPlace(place)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLocalPlaces(): List<Place> = withContext(Dispatchers.IO) {
        db.placeDao().getAllPlaces()
    }


    suspend fun syncPlacesFromFirestore() = withContext(Dispatchers.IO) {
        val snapshot = firestore.collection("places").get().await()
        val places = snapshot.toObjects(Place::class.java)
        Log.d("place", "syncPlacesFromFirestore: $places")
        db.placeDao().clearAll()
        places.forEach { db.placeDao().insertPlace(it) }
    }
}

