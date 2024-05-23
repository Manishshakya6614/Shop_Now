package com.manish.shopnow.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.shopnow.databinding.BestProductRvItemBinding
import com.manish.shopnow.model.ProductModel

class BestProductsAdapter(
    private val listener: OnBestProductClickListener
): RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

    inner class BestProductsViewHolder(private val binding: BestProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.apply {
                tvNewPrice.text = "₹${product.productSp}"
                tvPrice.paintFlags = tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                Glide.with(itemView).load(product.productCoverImg).into(imgProduct)
                tvPrice.text = "₹${product.productMrp}"
                tvName.text = product.productName
            }

        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.productId == newItem.productId

        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        return BestProductsViewHolder(
            BestProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            listener.onBestProductClick(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
interface OnBestProductClickListener {
    fun onBestProductClick(productModel: ProductModel)
}