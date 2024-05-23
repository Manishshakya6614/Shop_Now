package com.manish.shopnow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.shopnow.databinding.AllProductItemLayoutBinding
import com.manish.shopnow.model.ProductModel

class AllProductsAdapter(
    val context: Context,
    val list : ArrayList<ProductModel>,
    private val listener: OnClickProduct
): RecyclerView.Adapter<AllProductsAdapter.AllProductsViewHolder>() {

    inner class AllProductsViewHolder(val binding: AllProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductsViewHolder {
        val binding = AllProductItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return AllProductsViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AllProductsViewHolder, position: Int) {
        val product = list[position]
        Glide.with(context).load(product.productCoverImg).into(holder.binding.productImageIV)
        holder.binding.apply {
            tvProductNameAllOrderTV.text = product.productName
            tvProductSPAllOrder.text = "₹${product.productSp}"
            tvProductMrpAllOrder.text = "₹${product.productMrp}"
        }
        holder.binding.btnSeeProductAllOrder.setOnClickListener {
            listener.onProductClick(product)
        }
    }
}

interface OnClickProduct {
    fun onProductClick(productModel: ProductModel)
}