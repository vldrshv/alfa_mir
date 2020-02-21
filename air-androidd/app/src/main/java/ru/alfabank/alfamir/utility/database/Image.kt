package ru.alfabank.alfamir.utility.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Image(
        val guid: String,
        val path: String,
        val resolution: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}