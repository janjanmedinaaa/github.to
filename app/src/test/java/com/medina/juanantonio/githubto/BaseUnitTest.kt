package com.medina.juanantonio.githubto

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.medina.juanantonio.githubto.common.CoroutinesTestRule
import com.medina.juanantonio.githubto.mock.MockDatabaseManager
import com.medina.juanantonio.githubto.mock.MockNetworkManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Rule
import org.junit.rules.TestRule


abstract class BaseUnitTest {

    @JvmField
    @Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val mockNetworkManager = MockNetworkManager()

    val mockDatabaseManager = MockDatabaseManager()

    @After
    fun after() {
        MockDatabaseManager.users.clear()
    }
}