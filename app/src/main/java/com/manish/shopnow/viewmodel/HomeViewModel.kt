package com.manish.shopnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.manish.shopnow.adapters.CategoryAdapter
import com.manish.shopnow.model.CategoryModel
import com.manish.shopnow.model.ProductModel
import com.manish.shopnow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<ProductModel>>>(Resource.Unspecified())
    val specialProducts: StateFlow<Resource<List<ProductModel>>> = _specialProducts

    private val _bestDealsProducts =
        MutableStateFlow<Resource<List<ProductModel>>>(Resource.Unspecified())
    val bestDealsProducts: StateFlow<Resource<List<ProductModel>>> = _bestDealsProducts

    private val _bestProducts = MutableStateFlow<Resource<List<ProductModel>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<ProductModel>>> = _bestProducts

    private val pagingInfo = PagingInfo()

    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()
    }

    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("products")
            .whereEqualTo("productCategory", "Special Products").get().addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(ProductModel::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }


    fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDealsProducts.emit(Resource.Loading())
        }
        firestore.collection("products").whereEqualTo("productCategory", "Best Deals").get()
            .addOnSuccessListener { result ->
                val bestDealsProducts = result.toObjects(ProductModel::class.java)
                viewModelScope.launch {
                    _bestDealsProducts.emit(Resource.Success(bestDealsProducts))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealsProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
                firestore.collection("products").whereEqualTo("productCategory", "Best Products").limit(pagingInfo.bestProductsPage * 10).get()
                    .addOnSuccessListener { result ->
                        val bestProducts = result.toObjects(ProductModel::class.java)
                        pagingInfo.isPagingEnd = bestProducts == pagingInfo.oldBestProducts
                        pagingInfo.oldBestProducts = bestProducts
                        viewModelScope.launch {
                            _bestProducts.emit(Resource.Success(bestProducts))
                        }
                        pagingInfo.bestProductsPage++
                    }.addOnFailureListener {
                        viewModelScope.launch {
                            _bestProducts.emit(Resource.Error(it.message.toString()))
                        }
                    }
            }
        }
    }
//    fun showCategories(): ArrayList<CategoryModel> {
//        val list = ArrayList<CategoryModel>()
//        Firebase.firestore.collection("categories")
//            .get().addOnSuccessListener {
//                list.clear()
//                for (doc in it.documents) {
//                    val data = doc.toObject(CategoryModel::class.java)
//                    list.add(data!!)
//                }
//            }
//        return list
//    }


}

internal data class PagingInfo(
    var bestProductsPage: Long = 1,
    var oldBestProducts: List<ProductModel> = emptyList(),
    var isPagingEnd: Boolean = false
)