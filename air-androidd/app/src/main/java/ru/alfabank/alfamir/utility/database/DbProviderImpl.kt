package ru.alfabank.alfamir.utility.database

import androidx.room.Room
import android.content.Context
import javax.inject.Inject

class DbProviderImpl @Inject constructor(appContext: Context) : DbProvider {

    private val db = Room.databaseBuilder(appContext, AppDatabase::class.java, "cache").build()

    override fun getDb(): AppDatabase {
        return db
    }
}