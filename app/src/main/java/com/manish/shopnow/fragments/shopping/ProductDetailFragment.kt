package com.manish.shopnow.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.manish.shopnow.R
import com.manish.shopnow.databinding.FragmentProductDetailBinding
import com.manish.shopnow.model.CartProduct
import com.manish.shopnow.util.Resource
import com.manish.shopnow.viewmodel.CartDetailsViewModel
import com.manish.shopnow.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CartDetailsViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductDetailBinding.bind(view)

        val productModel = args.productModel

        getProductDetails(productModel!!.productId)

        binding.addToCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(productModel, 1))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.addToCart.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.addToCart.revertAnimation()
//                        binding.addToCart.setBackgroundColor(resources.getColor(R.color.black))
                        Toast.makeText(requireContext(), "Product Added to Cart", Toast.LENGTH_SHORT).show()

                    }

                    is Resource.Error -> {
                        binding.addToCart.stopAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }
    private fun getProductDetails(productId: String?) {
        Firebase.firestore.collection("products").document(productId!!).get()
            .addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                binding.productName.text = it.getString("productName")
                binding.productPrice.text = "₹" + it.getString("productSp")
                binding.productMrp.text = "₹" + it.getString("productMrp")
                binding.productDescription.text = it.getString("productDescription")

                val slideList = ArrayList<SlideModel>()
                for (data in list) {
                    slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                }
                binding.imageSlider.setImageList(slideList)

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}