package com.manish.shopnow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manish.shopnow.R
import com.manish.shopnow.databinding.LayoutCategoryListItemBinding
import com.manish.shopnow.model.CategoryModel

class CategoryAdapter(
    private var context: Context,
    private var list: ArrayList<CategoryModel>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = LayoutCategoryListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_list_item, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.categoryListItemTextView.text = list[position].cat
        Glide.with(context).load(list[position].img).into(holder.binding.categoryListItemImageView)

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(list[position].cat!!)
        }
    }
}
interface OnCategoryClickListener {
    fun onCategoryClick(productCat: String)
}