package com.medina.juanantonio.githubto.mock

import com.medina.juanantonio.githubto.data.manager.IDatabaseManager
import com.medina.juanantonio.githubto.data.model.User

class MockDatabaseManager : IDatabaseManager {

    companion object {
        val users = mutableListOf<User>()
    }

    override suspend fun addUsers(userList: List<User>) {
        userList.forEach { user ->
            val userExist = users.find { it.id == user.id }
            if (userExist != null) {
                val position = users.indexOfFirst { it.id == user.id }
                users[position] = user
            } else {
                users.add(user)
            }
        }
    }

    override suspend fun getUserList(): List<User> {
        return users
    }

    override suspend fun getUser(id: Int): User? {
        return users.find { it.id == id }
    }

    override suspend fun updateUserNote(id: Int, note: String) {
        val position = users.indexOf(
            users.find { it.id == id })
        users[position].note = note
    }

}