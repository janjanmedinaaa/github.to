package com.medina.juanantonio.githubto.features

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medina.juanantonio.githubto.R
import com.medina.juanantonio.githubto.data.manager.IDatabaseManager
import com.medina.juanantonio.githubto.databinding.ActivityMainBinding
import com.medina.juanantonio.githubto.features.profile.ProfileActivity
import com.medina.juanantonio.githubto.features.profile.ProfileActivity.Companion.USER_ID
import com.medina.juanantonio.githubto.network.INetworkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var networkManager: INetworkManager

    @Inject
    lateinit var databaseManager: IDatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = MainViewModel(networkManager, databaseManager)

        setSupportActionBar(binding.toolbar)

        userListAdapter = UserListAdapter(viewModel)
        binding.recyclerviewUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
            setItemViewCacheSize(20)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                    ) {
                        viewModel.viewModelScope.launch {
                            viewModel.getNewUsers()
                        }
                    }
                }
            })
        }

        viewModel.newUserList.observe(this, Observer {
            userListAdapter.addUsers(it)
        })

        viewModel.refreshUser.observe(this, Observer { (user, position) ->
            userListAdapter.refreshUser(user, position)
        })

        viewModel.user.observe(this, Observer { (user, position) ->
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra(USER_ID, user.id)
            }
            startActivityForResult(intent, position)
        })

        viewModel.showLoadingSpinner.observe(this, Observer {
            userListAdapter.run {
                if (it) {
                    addLoading()
                    binding.recyclerviewUsers.smoothScrollToPosition(
                        viewModel.userList.size + 1
                    )
                } else removeLoading()
            }
        })

        viewModel.viewModelScope.launch {
            viewModel.getInitialUserList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.viewModelScope.launch {
            viewModel.refreshUserItem(requestCode)
        }
    }

    override fun onInternetConnection(connected: Boolean) {
        binding.showNoInternetBanner = !connected
        if (connected && viewModel.userList.isEmpty())
            viewModel.viewModelScope.launch {
                viewModel.getInitialUserList()
            }
    }
}