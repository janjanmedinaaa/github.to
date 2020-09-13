package com.medina.juanantonio.githubto.network

import android.content.Context
import com.medina.juanantonio.githubto.R
import com.medina.juanantonio.githubto.common.InternetChecker
import com.medina.juanantonio.githubto.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class NetworkManager(
    private val context: Context,
    private val apiService: ApiService
) : INetworkManager {

    override suspend fun checkInternetConnection() = suspendCancellableCoroutine<Boolean> {
        val internetChecker = InternetChecker()
        it.invokeOnCancellation { internetChecker.cancelChecking() }
        internetChecker.hasInternetAccess(
            object : InternetChecker.InternetAccessCallback {
                override fun onInternetAccessResult(hasInternetAccess: Boolean) {
                    internetChecker.cancelChecking()
                    it.resume(hasInternetAccess)
                }
            }
        )
    }

    override suspend fun getUsers(since: Int): Result<List<User>> {
        val result: Result<List<User>>

        val hasInternet = checkInternetConnection()
        if (!hasInternet) return getDefaultErrorResponse(internetError = true)

        result = try {
            val response = withContext(Dispatchers.IO) {
                apiService.getUsers(since)
            }
            response.wrapWithResult()
        } catch (exception: Exception) {
            getDefaultErrorResponse()
        }
        return result
    }

    override suspend fun getUser(username: String): Result<User> {
        val result: Result<User>

        val hasInternet = checkInternetConnection()
        if (!hasInternet) return getDefaultErrorResponse(internetError = true)

        result = try {
            val response = withContext(Dispatchers.IO) {
                apiService.getUser(username)
            }
            response.wrapWithResult()
        } catch (exception: Exception) {
            getDefaultErrorResponse()
        }
        return result
    }

    private fun <T> getDefaultErrorResponse(internetError: Boolean = false): Result<T> {
        return if (internetError)
            Result.Error(-1, context.getString(R.string.no_internet_connection_message))
        else Result.Error(-2, context.getString(R.string.something_went_wrong))
    }
}

interface INetworkManager {
    suspend fun checkInternetConnection(): Boolean
    suspend fun getUsers(since: Int = 0): Result<List<User>>
    suspend fun getUser(username: String = ""): Result<User>
}