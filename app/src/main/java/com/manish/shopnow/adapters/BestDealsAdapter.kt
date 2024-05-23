package com.manish.shopnow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.shopnow.databinding.BestDealsRvItemBinding
import com.manish.shopnow.model.ProductModel

class BestDealsAdapter(private val listener: OnBestDealsClickListener) : RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>() {

    inner class BestDealsViewHolder(private val binding: BestDealsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.apply {
                Glide.with(itemView).load(product.productCoverImg).into(imgBestDeal)
                tvNewPrice.text = "₹${product.productSp}"
                tvOldPrice.text = "₹${product.productMrp}"
                tvDealProductName.text = product.productName
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(
            BestDealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            listener.onBestDealsCategoryClick(product)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}
interface OnBestDealsClickListener {
    fun onBestDealsCategoryClick(productModel: ProductModel)
}