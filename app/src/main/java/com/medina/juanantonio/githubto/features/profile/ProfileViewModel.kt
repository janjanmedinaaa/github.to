package com.medina.juanantonio.githubto.features.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medina.juanantonio.githubto.data.manager.IDatabaseManager
import com.medina.juanantonio.githubto.data.model.User
import com.medina.juanantonio.githubto.network.INetworkManager
import com.medina.juanantonio.githubto.network.Result
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val networkManager: INetworkManager,
    private val databaseManager: IDatabaseManager
) : ViewModel() {
    val user = MutableLiveData<User>()
    val userNote = MutableLiveData("")
    val showLoadingSpinner = MutableLiveData(false)

    suspend fun getUserFromLocalDb(id: Int, requestAPI: Boolean = true) {
        val localUser = databaseManager.getUser(id)
        user.value = localUser
        userNote.value = localUser?.note

        if (!requestAPI) return
        localUser?.login?.run { getUserFromAPI(this) }
    }

    suspend fun getUserFromAPI(username: String) {
        showLoadingSpinner.value = true
        val userResult = networkManager.getUser(username)

        if (userResult is Result.Success && userResult.data != null) {
            val userData = userResult.data
            val updateUser = userData.apply { note = user.value?.note ?: "" }

            databaseManager.addUsers(listOf(updateUser))
            user.value = updateUser
            userNote.value = updateUser.note
        }
        showLoadingSpinner.value = false
    }

    fun updateNote() {
        viewModelScope.launch {
            databaseManager.updateUserNote(
                user.value?.id ?: -1,
                userNote.value ?: ""
            )
            getUserFromLocalDb(user.value?.id ?: -1, requestAPI = false)
        }
    }
}

