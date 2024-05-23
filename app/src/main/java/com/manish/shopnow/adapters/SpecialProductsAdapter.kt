package com.manish.shopnow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.shopnow.databinding.SpecialRvItemBinding
import com.manish.shopnow.model.ProductModel

class SpecialProductsAdapter(private val listener: OnSpecialProductClickListener)
    : RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(val binding: SpecialRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.apply {
                Glide.with(itemView).load(product.productCoverImg).into(imageSpecialRvItem)
                tvSpecialProductName.text = product.productName
                tvSpecialPrdouctPrice.text = "₹${product.productSp}"
            }
        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.binding.btnAddToCart.setOnClickListener {
            listener.onSpecialProductClickListener(product)
        }
    }

}
interface OnSpecialProductClickListener {
    fun onSpecialProductClickListener(productModel: ProductModel)
}