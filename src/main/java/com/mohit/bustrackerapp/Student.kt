package com.mohit.bustrackerapp

import androidx.room.Entity

@Entity(primaryKeys = ["name"])
data class Student(
    val name: String,
)
