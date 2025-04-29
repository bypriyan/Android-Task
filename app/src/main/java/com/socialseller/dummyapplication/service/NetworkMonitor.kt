package com.socialseller.dummyapplication.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkMonitor(
    private val context: Context,
    private val onAvailable: () -> Unit
) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var isRegistered = false

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            onAvailable()
        }
    }

    fun register() {
        if (!isRegistered) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
            isRegistered = true
        }
    }

    fun unregister() {
        if (isRegistered) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
            isRegistered = false
        }
    }
}
