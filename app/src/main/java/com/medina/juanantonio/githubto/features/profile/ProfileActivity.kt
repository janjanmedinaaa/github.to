package com.medina.juanantonio.githubto.features.profile

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.medina.juanantonio.githubto.R
import com.medina.juanantonio.githubto.data.manager.IDatabaseManager
import com.medina.juanantonio.githubto.databinding.ActivityProfileBinding
import com.medina.juanantonio.githubto.features.BaseActivity
import com.medina.juanantonio.githubto.network.INetworkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {

    companion object {
        const val USER_ID = "USER_ID"
    }

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    @Inject
    lateinit var networkManager: INetworkManager

    @Inject
    lateinit var databaseManager: IDatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.lifecycleOwner = this
        viewModel = ProfileViewModel(networkManager, databaseManager)
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        viewModel.user.observe(this, Observer { user ->
            Glide
                .with(this)
                .load(user.avatar_url)
                .centerCrop()
                .into(binding.imageviewUserProfile)
        })

        viewModel.showLoadingSpinner.observe(this, Observer {
            binding.showLoadingSpinner = it
        })

        viewModel.viewModelScope.launch {
            val userId = intent.getIntExtra(USER_ID, -1)
            viewModel.getUserFromLocalDb(userId)
        }

        binding.buttonSave.setOnClickListener {
            viewModel.updateNote()
            Toast.makeText(
                this,
                getString(R.string.note_updated),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onInternetConnection(connected: Boolean) {
        binding.showNoInternetBanner = !connected
        if (connected) viewModel.viewModelScope.launch {
            viewModel.getUserFromAPI(viewModel.user.value?.login ?: "")
        }
    }
}