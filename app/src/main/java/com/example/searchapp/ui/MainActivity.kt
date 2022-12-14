package com.example.searchapp.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.searchapp.R
import com.example.searchapp.databinding.ActivityMainBinding
import com.example.searchapp.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkConnection()
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    Log.i(Constants.TAGM, "Key enter")
                    try {
                        startActivity(
                            Intent(
                                this,
                                ProductsActivity::class.java
                            ).putExtra(Constants.EXTRA_SEARCH, binding.etSearch.text.toString())
                        )
                    } catch (e: Exception) {
                        Log.i(Constants.TAGM, "${e.message}")
                        showMessage(getString(R.string.main_error_intent))
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun showMessage(error: String?) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkConnection(): Boolean {
        val manager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.getNetworkCapabilities(manager.activeNetwork)
        if (networkInfo != null) {
            if (networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true
            else if (networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true
        } else {
            val builder = AlertDialog.Builder(this)
                .setTitle(R.string.dialog_connection_title)
                .setMessage(getString(R.string.dialog_connection_message))
                .setIcon(R.drawable.ic_signal_wifi_connected_no_internet)
                .setPositiveButton(getString(R.string.dialog_ok)) { _, _ ->
                    // Nothing to implement
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return false
    }
}
