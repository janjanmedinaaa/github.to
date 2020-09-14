package com.medina.juanantonio.githubto.features

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medina.juanantonio.githubto.common.extensions.toArrayList
import com.medina.juanantonio.githubto.data.manager.IDatabaseManager
import com.medina.juanantonio.githubto.data.model.User
import com.medina.juanantonio.githubto.network.INetworkManager
import com.medina.juanantonio.githubto.network.Result

class MainViewModel(
    private val networkManager: INetworkManager,
    private val databaseManager: IDatabaseManager
) : ViewModel(), UserListAdapter.UserItemListener {
    val newUserList = MutableLiveData<ArrayList<User>>()
    val user = MutableLiveData<Pair<User, Int>>()
    val refreshUser = MutableLiveData<Pair<User, Int>>()
    val showLoadingSpinner = MutableLiveData(false)
    var userList = arrayListOf<User>()
    private var requestOnGoing = false

    suspend fun getInitialUserList() {
        val currentUserList = databaseManager.getUserList().toArrayList()
        this.newUserList.value = currentUserList
        userList.addAll(currentUserList)

        if (userList.isNotEmpty()) return

        getNewUsers()
    }

    suspend fun getNewUsers() {
        if (requestOnGoing) return
        requestOnGoing = true

        showLoadingSpinner.value = true
        val currentLastId = if (userList.isEmpty()) 0 else userList.last().id
        val userListResult = networkManager.getUsers(since = currentLastId)

        if (userListResult is Result.Success) {
            val newUserList = userListResult.data
            userList.addAll(newUserList ?: listOf())
            databaseManager.addUsers(newUserList ?: listOf())

            showLoadingSpinner.value = false
            this.newUserList.value = newUserList?.toArrayList()
        } else {
            showLoadingSpinner.value = false
        }
        requestOnGoing = false
    }

    suspend fun refreshUserItem(position: Int) {
        val refreshedUser = databaseManager.getUser(userList[position].id)
        refreshedUser?.let {
            refreshUser.value = Pair(refreshedUser, position)
        }
    }

    override fun onItemClicked(item: User, position: Int) {
        user.value = Pair(item, position)
    }
}