package com.medina.juanantonio.githubto.network

import com.medina.juanantonio.githubto.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int = 0
    ): Response<List<User>>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String = ""
    ): Response<User>
}