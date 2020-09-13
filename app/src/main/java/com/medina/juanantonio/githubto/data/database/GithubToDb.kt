package com.medina.juanantonio.githubto.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medina.juanantonio.githubto.BuildConfig
import com.medina.juanantonio.githubto.data.database.dao.UserDao
import com.medina.juanantonio.githubto.data.model.User

@Database(
    entities = [
        User::class
    ],
    version = BuildConfig.VERSION_CODE,
    exportSchema = false
)
abstract class GithubToDb : RoomDatabase() {
    abstract fun userDao(): UserDao
}