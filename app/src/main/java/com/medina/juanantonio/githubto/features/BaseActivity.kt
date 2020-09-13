package com.medina.juanantonio.githubto.features

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.medina.juanantonio.githubto.common.InternetChecker

open class BaseActivity : AppCompatActivity(), InternetChecker.InternetAccessCallback {
    private var networkCallbackRegistered = false
    private val internetChecker = InternetChecker()
    private val networkListener = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            onInternetConnection(connected = false)
        }

        override fun onAvailable(network: Network) {
            internetChecker.hasInternetAccess(this@BaseActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerInternetListener(networkListener)
        internetChecker.hasInternetAccess(this)
    }

    override fun onDestroy() {
        unregisterInternetListener(networkListener)
        super.onDestroy()
    }

    override fun onInternetAccessResult(hasInternetAccess: Boolean) {
        onInternetConnection(connected = hasInternetAccess)
    }

    open fun onInternetConnection(connected: Boolean) {}

    private fun registerInternetListener(listener: ConnectivityManager.NetworkCallback) {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(listener)
        networkCallbackRegistered = true
    }

    private fun unregisterInternetListener(listener: ConnectivityManager.NetworkCallback) {
        if (!networkCallbackRegistered) return
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(listener)
        networkCallbackRegistered = false
    }
}