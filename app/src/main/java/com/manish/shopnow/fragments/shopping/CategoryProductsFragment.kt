package com.manish.shopnow.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kelineyt.util.VerticalItemDecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.manish.shopnow.R
import com.manish.shopnow.adapters.CategoryProductsAdapter
import com.manish.shopnow.adapters.OnCategoryProductClickListener
import com.manish.shopnow.databinding.FragmentCategoryProductsBinding
import com.manish.shopnow.model.ProductModel
import com.manish.shopnow.util.HorizontalItemDecoration


class CategoryProductsFragment : Fragment(R.layout.fragment_category_products), OnCategoryProductClickListener {

    private lateinit var binding: FragmentCategoryProductsBinding
    private val args: CategoryProductsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCategoryProductsBinding.bind(view)

        val productCat = args.cat

        getProducts(productCat)
    }

    private fun getProducts(category: String?) {
        val list = ArrayList<ProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory", category)
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(ProductModel::class.java)
                    list.add(data!!)
                }
                binding.recyclerView.adapter = CategoryProductsAdapter(requireContext(), list, this)
                binding.recyclerView.addItemDecoration(VerticalItemDecoration())
                binding.recyclerView.addItemDecoration(HorizontalItemDecoration())
            }
    }

    override fun onCategoryProductClick(productModel: ProductModel) {
        val bundle = Bundle().apply {
            putSerializable("productModel", productModel)
        }
        findNavController().navigate(R.id.action_categoryProductsFragment_to_productDetailFragment,bundle)
    }

}