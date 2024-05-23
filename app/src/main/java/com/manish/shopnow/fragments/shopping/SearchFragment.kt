package com.manish.shopnow.fragments.shopping

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.manish.shopnow.R
import com.manish.shopnow.adapters.SearchProductAdapter
import com.manish.shopnow.databinding.FragmentSearchBinding
import com.manish.shopnow.model.ProductModel

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: SearchProductAdapter
    private val productList = mutableListOf<ProductModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        binding.etSearchSearch.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearchSearch, InputMethodManager.SHOW_IMPLICIT)

        firestore = FirebaseFirestore.getInstance()
        adapter = SearchProductAdapter(productList)

        binding.recyclerViewSearch.adapter = adapter

        binding.etSearchSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fetchProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchProducts(query: String) {
        if (query.isEmpty()) {
            productList.clear()
            adapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        firestore.collection("products")
            .whereGreaterThanOrEqualTo("productName", query)
            .whereLessThanOrEqualTo("productName", query + "\uf8ff")
            .get()
            .addOnSuccessListener { documents ->
                productList.clear()
                for (document in documents) {
                    val product = document.toObject(ProductModel::class.java)
                    productList.add(product)
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
            }
    }
}