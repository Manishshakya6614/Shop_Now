package com.manish.shopnow.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CartProduct(
    val productModel: ProductModel,
    val quantity: Int
): Parcelable {
    constructor() : this(ProductModel(), 1)
}
