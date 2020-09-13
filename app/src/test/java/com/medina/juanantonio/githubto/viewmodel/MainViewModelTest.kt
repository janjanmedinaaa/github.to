package com.medina.juanantonio.githubto.viewmodel

import com.medina.juanantonio.githubto.BaseUnitTest
import com.medina.juanantonio.githubto.features.MainViewModel
import com.medina.juanantonio.githubto.mock.MockDatabaseManager
import com.medina.juanantonio.githubto.mock.TestConstants.getMockUsersList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: MainViewModel

    @Before
    fun before() {
        viewModel = MainViewModel(
            mockNetworkManager,
            mockDatabaseManager
        )

        MockDatabaseManager.users.addAll(getMockUsersList())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getInitialUserList() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.getInitialUserList()
        assertEquals(getMockUsersList()[0].login, viewModel.userList[0].login)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getNewUsers() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.getInitialUserList()
        viewModel.getNewUsers()

        assertEquals(10, viewModel.userList.last().id)
        assertEquals("testuser10", MockDatabaseManager.users.last().login)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun refreshUserItem() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.getInitialUserList()
        viewModel.refreshUserItem(position = 4)

        assertEquals("testuser5", viewModel.refreshUser.value?.first?.login)
        assertEquals(4, viewModel.refreshUser.value?.second)
    }
}