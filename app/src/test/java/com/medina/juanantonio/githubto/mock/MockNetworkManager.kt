package com.medina.juanantonio.githubto.mock

import com.medina.juanantonio.githubto.data.model.User
import com.medina.juanantonio.githubto.mock.TestConstants.getMockUsersList
import com.medina.juanantonio.githubto.mock.TestConstants.NetworkErrorMessage.NO_INTERNET_CONNECTION
import com.medina.juanantonio.githubto.network.INetworkManager
import com.medina.juanantonio.githubto.network.Result

class MockNetworkManager : INetworkManager {

    companion object {
        var hasInternet = true
        private const val DEFAULT_STATUS_CODE = -1
    }

    override suspend fun checkInternetConnection(): Boolean {
        return hasInternet
    }

    override suspend fun getUsers(since: Int): Result<List<User>> {
        if (!hasInternet) {
            return Result.Error(DEFAULT_STATUS_CODE, NO_INTERNET_CONNECTION)
        }
        return Result.Success(getMockUsersList(since))
    }

    override suspend fun getUser(username: String): Result<User> {
        if (!hasInternet) {
            return Result.Error(DEFAULT_STATUS_CODE, NO_INTERNET_CONNECTION)
        }
        return Result.Success(getMockUsersList(detailed = true).find { it.login == username })
    }
}