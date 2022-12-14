package com.example.searchapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchapp.R
import com.example.searchapp.databinding.ProductItemBinding
import com.example.searchapp.models.ProductResponse
import com.example.searchapp.retrofit.OnClickListener
import com.example.searchapp.utils.formatAmountTo

class ProductListAdapter(
    private val products: List<ProductResponse>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.setListener(product)
        with(holder.binding) {
            tvTitle.text = product.title
            tvPrice.text = formatAmountTo(product.price.toString())
            tvCondition.text = product.condition
            Glide.with(context).load(product.image).into(imgCardView)
        }
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ProductItemBinding.bind(view)

        // function to get a product by item in the recycler view
        fun setListener(product: ProductResponse) {
            binding.root.setOnClickListener { listener.onClick(product) }
        }
    }
}
