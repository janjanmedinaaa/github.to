package com.medina.juanantonio.githubto.data.manager

import com.medina.juanantonio.githubto.data.database.GithubToDb
import com.medina.juanantonio.githubto.data.model.User

class DatabaseManager(githubToDb: GithubToDb) : IDatabaseManager {
    private val userDao = githubToDb.userDao()

    override suspend fun addUsers(userList: List<User>) {
        userList.forEach { userDao.insert(it) }
    }

    override suspend fun getUserList(): List<User> {
        return userDao.getUsers() ?: listOf()
    }

    override suspend fun getUser(id: Int): User? {
        return userDao.getUser(id)
    }

    override suspend fun updateUserNote(id: Int, note: String) {
        userDao.updateUserNote(id, note)
    }
}

interface IDatabaseManager {
    suspend fun addUsers(userList: List<User>)
    suspend fun getUserList(): List<User>
    suspend fun getUser(id: Int): User?
    suspend fun updateUserNote(id: Int, note: String)
}