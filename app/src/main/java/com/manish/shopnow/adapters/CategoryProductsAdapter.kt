package com.manish.shopnow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.shopnow.databinding.ItemCategoryProductLayoutBinding
import com.manish.shopnow.model.ProductModel

class CategoryProductsAdapter(
    val context: Context,
    val list : ArrayList<ProductModel>,
    private val listener: OnCategoryProductClickListener
) : RecyclerView.Adapter<CategoryProductsAdapter.CategoryProductViewHolder>() {

    inner class CategoryProductViewHolder(val binding: ItemCategoryProductLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding = ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return CategoryProductViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        val data = list[position]
        Glide.with(context).load(data.productCoverImg).into(holder.binding.productListItemCoverImg)

        holder.binding.apply {
            productListItemProductName.text = data.productName
            productListItemCategoryName.text = data.productCategory
            productListItemBtnSP.text = "₹${data.productSp}"
        }
        holder.itemView.setOnClickListener {
            listener.onCategoryProductClick(data)
        }
    }
}
interface OnCategoryProductClickListener {
    fun onCategoryProductClick(productModel: ProductModel)
}