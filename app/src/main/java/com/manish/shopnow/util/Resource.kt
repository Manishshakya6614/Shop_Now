package com.manish.shopnow.util

// This class is a generic class, it means it can receive any data type
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null // Error message in case something went wrong
) {
    // Subclass for success state
    class Success<T>(data: T) : Resource<T>(data)
    // Subclass for loading loading
    class Loading<T> : Resource<T>()
    // Subclass for error state
    class Error<T>(message: String) : Resource<T>(message = message)
    // Subclass for unspecified state
    class Unspecified<T> : Resource<T>()
}