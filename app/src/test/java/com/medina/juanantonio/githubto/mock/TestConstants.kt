package com.medina.juanantonio.githubto.mock

import com.medina.juanantonio.githubto.data.model.User

object TestConstants {

    object NetworkErrorMessage {
        const val NO_INTERNET_CONNECTION =
            "Please check your internet connection and try again later."
    }

    fun getMockUsersList(startingId: Int = 0, detailed: Boolean = false): List<User> {
        return listOf(
            User().apply {
                val newId = startingId + 1
                id = newId
                login = "testuser$newId"
                html_url = "https://github.com/testuser$newId"
                name = if (detailed) "Test User $newId" else null
                company = if (detailed) "Test User $newId Company" else null
                blog = if (detailed) "Test User $newId Blog" else null
                bio = if (detailed) "Test User $newId Bio" else null
            },
            User().apply {
                val newId = startingId + 2
                id = newId
                login = "testuser$newId"
                html_url = "https://github.com/testuser$newId"
                name = if (detailed) "Test User $newId" else null
                company = if (detailed) "Test User $newId Company" else null
                blog = if (detailed) "Test User $newId Blog" else null
                bio = if (detailed) "Test User $newId Bio" else null
            },
            User().apply {
                val newId = startingId + 3
                id = newId
                login = "testuser$newId"
                html_url = "https://github.com/testuser$newId"
                name = if (detailed) "Test User $newId" else null
                company = if (detailed) "Test User $newId Company" else null
                blog = if (detailed) "Test User $newId Blog" else null
                bio = if (detailed) "Test User $newId Bio" else null
            },
            User().apply {
                val newId = startingId + 4
                id = newId
                login = "testuser$newId"
                html_url = "https://github.com/testuser$newId"
                name = if (detailed) "Test User $newId" else null
                company = if (detailed) "Test User $newId Company" else null
                blog = if (detailed) "Test User $newId Blog" else null
                bio = if (detailed) "Test User $newId Bio" else null
            },
            User().apply {
                val newId = startingId + 5
                id = newId
                login = "testuser$newId"
                html_url = "https://github.com/testuser$newId"
                name = if (detailed) "Test User $newId" else null
                company = if (detailed) "Test User $newId Company" else null
                blog = if (detailed) "Test User $newId Blog" else null
                bio = if (detailed) "Test User $newId Bio" else null
            }
        )
    }
}