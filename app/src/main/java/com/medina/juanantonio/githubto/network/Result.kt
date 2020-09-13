package com.medina.juanantonio.githubto.network

import org.json.JSONObject
import retrofit2.Response

// A generic class that contains data and status about loading this data.
sealed class Result<T>(
    val data: T? = null,
    val message: String? = null,
    val statusCode: Int = 0
) {
    class Loading<T> : Result<T>()
    class Success<T>(data: T?) : Result<T>(data)
    class Error<T>(
        statusCode: Int,
        message: String,
        data: T? = null
    ) : Result<T>(data, message, statusCode)

    class Cancelled<T> : Result<T>()
}

fun <T> Result.Error<T>.getServerMessage(): String {
    return try {
        val jsonObject = JSONObject(message ?: "{}")
        jsonObject.getString("message")
    } catch (exception: Exception) {
        message ?: statusCode.toString()
    }
}

/**
 *  Added extension function of 'Retrofit Response Class' to wrap
 *  network response with our own Result.kt
 *  @see retrofit2.Response
 */
fun <RESPONSE_TYPE> Response<RESPONSE_TYPE>.wrapWithResult(): Result<RESPONSE_TYPE> {
    return if (isSuccessful) Result.Success(body())
    else Result.Error(code(), errorBody()?.string() ?: "")
}