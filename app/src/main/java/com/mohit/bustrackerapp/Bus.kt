package com.mohit.bustrackerapp

import androidx.room.Entity

@Entity("bus", primaryKeys = ["name"])
data class Bus(
    val name: String,
    val latitude: Float,
    val longitude: Float
)