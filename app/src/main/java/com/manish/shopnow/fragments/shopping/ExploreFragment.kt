package com.manish.shopnow.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.manish.shopnow.R
import com.manish.shopnow.adapters.AllProductsAdapter
import com.manish.shopnow.adapters.CategoryProductsAdapter
import com.manish.shopnow.adapters.OnClickProduct
import com.manish.shopnow.databinding.FragmentExploreBinding
import com.manish.shopnow.model.ProductModel


class ExploreFragment : Fragment(R.layout.fragment_explore), OnClickProduct {
    private lateinit var binding: FragmentExploreBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExploreBinding.bind(view)

        getProducts()
    }
    private fun getProducts() {
        val list = ArrayList<ProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(ProductModel::class.java)
                    list.add(data!!)
                }
                binding.rvProducts.adapter = AllProductsAdapter(requireContext(), list, this)
            }
    }

    override fun onProductClick(productModel: ProductModel) {
        val bundle = Bundle().apply {
            putSerializable("productModel", productModel)
        }
        findNavController().navigate(R.id.action_exploreFragment_to_productDetailFragment,bundle)
    }
}