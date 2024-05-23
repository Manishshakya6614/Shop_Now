package com.manish.shopnow.fragments.shopping

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kelineyt.util.VerticalItemDecoration
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.manish.shopnow.R
import com.manish.shopnow.adapters.BestDealsAdapter
import com.manish.shopnow.adapters.BestProductsAdapter
import com.manish.shopnow.adapters.CategoryAdapter
import com.manish.shopnow.adapters.OnBestDealsClickListener
import com.manish.shopnow.adapters.OnBestProductClickListener
import com.manish.shopnow.adapters.OnCategoryClickListener
import com.manish.shopnow.adapters.OnSpecialProductClickListener
import com.manish.shopnow.adapters.SpecialProductsAdapter
import com.manish.shopnow.databinding.FragmentHomeBinding
import com.manish.shopnow.model.CategoryModel
import com.manish.shopnow.model.ProductModel
import com.manish.shopnow.util.HorizontalItemDecoration
import com.manish.shopnow.util.Resource
import com.manish.shopnow.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategoryFragment"
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    OnCategoryClickListener,
    OnSpecialProductClickListener,
    OnBestDealsClickListener,
    OnBestProductClickListener
{

    private lateinit var binding: FragmentHomeBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        // Setting Slider Images
        getSliderImages()

        // Setting Categories Recycler View
//        val list = viewModel.showCategories()
//        binding.categoryRecyclerView.adapter = CategoryAdapter(requireContext(), list, this)
        showCategories()

        setUpSpecialProductsRV()

        setupBestDealsRv()

        setupBestProductsRV()

        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDealsProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductsProgressbar.visibility = View.GONE


                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.bestProductsProgressbar.visibility = View.GONE

                    }
                    else -> Unit
                }
            }
        }
        binding.etSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun showLoading() {
        binding.homeProgressbar.visibility = View.VISIBLE
    }

    private fun setUpSpecialProductsRV() {
        specialProductsAdapter = SpecialProductsAdapter(this)
        binding.specialProductsRecyclerView.apply {
            adapter = specialProductsAdapter
            addItemDecoration(VerticalItemDecoration())
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBestDealsRv() {
        bestDealsAdapter = BestDealsAdapter(this)
        binding.rvBestDealsProducts.apply {
            adapter = bestDealsAdapter
            addItemDecoration(VerticalItemDecoration())
            addItemDecoration(HorizontalItemDecoration())
        }
    }
    private fun setupBestProductsRV() {
        bestProductsAdapter = BestProductsAdapter(this)
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
            addItemDecoration(VerticalItemDecoration())
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun hideLoading() {
        binding.homeProgressbar.visibility = View.GONE
    }

    private fun showCategories() {
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecyclerView.adapter = CategoryAdapter(requireContext(), list, this)
            }
    }

    private fun getSliderImages() {
        Firebase.firestore.collection("sliders").get()
            .addOnSuccessListener {
                val slideList = ArrayList<SlideModel>()
                for (doc in it.documents) {
                    slideList.add(SlideModel(doc.getString("img"), ScaleTypes.CENTER_CROP))
                }
                binding.sliderHomeImageView.setImageList(slideList)
            }
    }

    override fun onCategoryClick(productCat: String) {
        val bundle = Bundle().apply {
            putSerializable("cat",productCat)
        }
        findNavController().navigate(R.id.action_homeFragment_to_categoryProductsFragment,bundle)
    }

    override fun onSpecialProductClickListener(productModel: ProductModel) {
        val bundle = Bundle().apply {
            putSerializable("productModel",productModel)
        }
        findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,bundle)
    }

    override fun onBestDealsCategoryClick(productModel: ProductModel) {
        val bundle = Bundle().apply {
            putSerializable("productModel",productModel)
        }
        findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,bundle)
    }

    override fun onBestProductClick(productModel: ProductModel) {
        val bundle = Bundle().apply {
            putSerializable("productModel",productModel)
        }
        findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment,bundle)
    }

}