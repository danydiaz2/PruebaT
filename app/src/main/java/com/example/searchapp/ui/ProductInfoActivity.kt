package com.example.searchapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.searchapp.R
import com.example.searchapp.databinding.ActivityProductInfoBinding
import com.example.searchapp.models.ProductResponse
import com.example.searchapp.utils.Constants
import com.example.searchapp.utils.formatAmountTo

class ProductInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the data of the product from previous screen
        val product: ProductResponse = intent.getParcelableExtra(Constants.EXTRA_PRODUCT)!!

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.product_info_title)

        // Set the product data into layout with binding
        with(binding) {
            tvConditionInfo.text = product.condition
            tvTitleInfo.text = product.title
            tvPrice.text = formatAmountTo(product.price.toString())
            Glide.with(this@ProductInfoActivity).load(product.image).into(imgView)
        }
    }

    // Enable back function
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
}
