package ru.alfabank.alfamir.utility.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Image::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao
}