package com.mohit.bustrackerapp;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Bus::class, Student::class], version = 1)
abstract class BusTrackerDatabase : RoomDatabase() {
    abstract fun userDao(): BusDao

    companion object {
        private var db: BusTrackerDatabase? = null
        fun getDb(context: Context): BusTrackerDatabase {
            if (db == null) {
                db = Room.databaseBuilder(context, BusTrackerDatabase::class.java, "BusTrackerDatabase").allowMainThreadQueries().build()
            }

            return db!!
        }

    }
}