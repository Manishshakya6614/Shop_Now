package com.manish.shopnow.util

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manish.shopnow.activities.ShoppingActivity

fun Fragment.hideBottomNavigationView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
            com.manish.shopnow.R.id.bottomNavBar
        )
    bottomNavigationView.visibility = android.view.View.GONE
}

fun Fragment.showBottomNavigationView(){
    val bottomNavigationView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(
            com.manish.shopnow.R.id.bottomNavBar
        )
    bottomNavigationView.visibility = android.view.View.VISIBLE
}