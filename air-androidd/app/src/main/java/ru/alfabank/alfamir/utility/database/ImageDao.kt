package ru.alfabank.alfamir.utility.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image: Image)

    @Query("SELECT * FROM image WHERE guid = :guid")
    fun getImage(guid: String): Image?

    @Query("SELECT * FROM image WHERE guid = :guid AND resolution = :resolution")
    fun getImage(guid: String, resolution: String): Image?
}