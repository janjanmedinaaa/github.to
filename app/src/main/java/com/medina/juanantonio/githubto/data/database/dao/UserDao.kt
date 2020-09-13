package com.medina.juanantonio.githubto.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medina.juanantonio.githubto.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>?

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUser(id: Int): User?

    @Query("UPDATE user SET note = :note WHERE id = :id")
    suspend fun updateUserNote(id: Int, note: String = "")
}