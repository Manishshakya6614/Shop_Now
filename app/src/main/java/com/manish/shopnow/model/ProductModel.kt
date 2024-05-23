package com.manish.shopnow.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class ProductModel(
    val productName: String? = "",
    val productDescription: String? = "",
    val productCoverImg: String? = "",
    val productCategory: String? = "",
    val productId: String? = "",
    val productMrp: String? = "",
    val productSp: String? = "",
    val productImages: ArrayList<String>
): Serializable {
    constructor(): this("", "", "", "", "", "", "",  ArrayList())
}