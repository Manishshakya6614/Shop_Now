package com.manish.shopnow.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Address(
    val addressTitle: String,
    val fullName: String,
    val street: String,
    val phoneNo: String,
    val city: String,
    val state: String
): Parcelable {
    constructor(): this("", "", "", "", "", "")
}
