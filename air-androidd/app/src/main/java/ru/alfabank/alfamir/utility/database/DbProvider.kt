package ru.alfabank.alfamir.utility.database

interface DbProvider {

    fun getDb(): AppDatabase
}