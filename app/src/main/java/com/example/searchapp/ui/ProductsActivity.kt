package com.example.searchapp.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchapp.R
import com.example.searchapp.adapters.ProductListAdapter
import com.example.searchapp.databinding.ActivityProductsBinding
import com.example.searchapp.models.ProductResponse
import com.example.searchapp.models.ResultResponse
import com.example.searchapp.retrofit.OnClickListener
import com.example.searchapp.retrofit.ProductApiClient
import com.example.searchapp.retrofit.RetrofitHelper
import com.example.searchapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ProductsActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityProductsBinding
    private lateinit var adapter: ProductListAdapter
    private var products = listOf<ProductResponse>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the parameter from the previous screen
        val search = intent.getStringExtra("EXTRA_SEARCH")

        // Set the title and back icon on the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.product_list_title)

        checkConnection()
        setUpRecyclerView()
        searchByProduct(search!!)
    }

    private fun setUpRecyclerView() {
        adapter = ProductListAdapter(products, this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductsActivity)
            adapter = this@ProductsActivity.adapter
            setHasFixedSize(true)
        }
    }

    private fun searchByProduct(query: String) {
        val call = RetrofitHelper.getRetrofit().create(ProductApiClient::class.java)
            .getProductsBySearch(query)
        call.enqueue(
            object : Callback<ResultResponse> {
                override fun onResponse(
                    call: Call<ResultResponse>,
                    response: Response<ResultResponse>
                ) {
                    runOnUiThread {
                        try {
                            if (response.isSuccessful) {
                                products = response.body()!!.products
                                adapter = ProductListAdapter(products, this@ProductsActivity)
                                binding.recyclerView.adapter = adapter
                                binding.recyclerView.setHasFixedSize(true)
                                binding.progressBar.visibility = View.GONE
                                Log.i(
                                    Constants.TAGP,
                                    " onResponse: ${response.body()!!.products[0].title}, ${response.code()}"
                                )
                            }
                        } catch (e: Exception) {
                            (e as? HttpException)?.let { checkErrorHttp(e) }
                        }
                    }
                }

                override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                    Log.i(Constants.TAGP, "Algo salió mal: ${t.message}")
                    val builder = AlertDialog.Builder(this@ProductsActivity)
                        .setTitle(R.string.dialog_error_title)
                        .setMessage(R.string.dialog_error_try)
                        .setIcon(R.drawable.ic_signal_wifi_connected_no_internet)
                        .setPositiveButton(R.string.dialog_ok) { _, _ ->
                            onBackPressed()
                        }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
        )
    }

    private fun checkErrorHttp(e: HttpException) {
        when (e.code()) {
            400 -> Toast.makeText(this, "${R.string.main_error_server}", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "${R.string.main_error_response}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Function to get the product on the recycler view and go to another screen with the data
    override fun onClick(product: ProductResponse) {
        val intent =
            Intent(this, ProductInfoActivity::class.java).putExtra(Constants.EXTRA_PRODUCT, product)
        startActivity(intent)
        Log.i(Constants.TAGP, product.title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
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
                .setMessage(R.string.dialog_connection_message)
                .setIcon(R.drawable.ic_signal_wifi_connected_no_internet)
                .setPositiveButton(R.string.dialog_ok) { _, _ ->
                    onBackPressed()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        return false
    }
}
