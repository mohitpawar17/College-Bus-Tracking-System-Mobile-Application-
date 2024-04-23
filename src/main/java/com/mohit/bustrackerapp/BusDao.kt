package com.mohit.bustrackerapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BusDao {
    @Query("SELECT * FROM bus")
    suspend fun getAllBuses(): List<Bus>

    @Query("SELECT * FROM student")
    suspend fun getAllStudents(): List<Student>

    @Query("SELECT * FROM bus WHERE name = :name")
    fun getBus(name: String): Bus?

    @Update
    suspend fun updateLocation(bus: Bus)

    @Insert
    suspend fun insertBus(bus: Bus)

    @Insert
    fun insertStudent(student: Student)
}
