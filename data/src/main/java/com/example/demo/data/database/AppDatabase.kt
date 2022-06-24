package com.example.demo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demo.data.model.github.UserEntity

@Database(entities = [UserEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}