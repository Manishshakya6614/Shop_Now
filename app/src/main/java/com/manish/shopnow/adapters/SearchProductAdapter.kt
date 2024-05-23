package com.manish.shopnow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.shopnow.databinding.ItemProductSearchBinding
import com.manish.shopnow.model.ProductModel

class SearchProductAdapter(private val productList: List<ProductModel>)
    : RecyclerView.Adapter<SearchProductAdapter.SearchProductViewHolder>(){

        inner class SearchProductViewHolder(val binding: ItemProductSearchBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(product: ProductModel) {
                binding.apply {
                    Glide.with(itemView).load(product.productCoverImg).into(imgProductSearch)
                    tvProductNameSearch.text = product.productName
                    tvProductSellingPriceSearch.text = "₹${product.productSp}"
                    tvProductMrpSearch.text = "₹${product.productMrp}"
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductViewHolder {
        return SearchProductViewHolder(
            ItemProductSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }


}