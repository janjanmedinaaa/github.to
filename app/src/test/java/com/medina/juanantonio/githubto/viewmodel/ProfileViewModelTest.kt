package com.medina.juanantonio.githubto.viewmodel

import com.medina.juanantonio.githubto.BaseUnitTest
import com.medina.juanantonio.githubto.features.profile.ProfileViewModel
import com.medina.juanantonio.githubto.mock.MockDatabaseManager
import com.medina.juanantonio.githubto.mock.TestConstants.getMockUsersList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest : BaseUnitTest() {
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun before() {
        viewModel = ProfileViewModel(
            mockNetworkManager,
            mockDatabaseManager
        )

        MockDatabaseManager.users.addAll(getMockUsersList())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getUserFromLocalDb() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.getUserFromLocalDb(id = 2, requestAPI = false)

        assertEquals("testuser2", viewModel.user.value?.login)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateNote() = coroutinesTestRule.testDispatcher.runBlockingTest {
        viewModel.getUserFromLocalDb(id = 1, requestAPI = false)

        viewModel.userNote.value = "This is a note."
        viewModel.updateNote()

        assertEquals("This is a note.", MockDatabaseManager.users[0].note)
    }
}