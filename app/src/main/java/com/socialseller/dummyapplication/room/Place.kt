package com.socialseller.dummyapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "places")
data class Place(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
){
    // Firestore requires this for deserialization
    constructor() : this("", "", "", "", 0.0, 0.0)
}
