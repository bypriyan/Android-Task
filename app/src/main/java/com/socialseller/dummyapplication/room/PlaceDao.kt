package com.socialseller.dummyapplication.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: Place)

    @Query("DELETE FROM places")
    suspend fun clearAll()

    @Query("SELECT * FROM places")
    suspend fun getAllPlaces(): List<Place>
}
