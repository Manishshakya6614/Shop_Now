package com.manish.shopnow.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.util.VerticalItemDecoration
import com.manish.shopnow.R
import com.manish.shopnow.adapters.BillingProductsAdapter
import com.manish.shopnow.databinding.FragmentOrderDetailBinding
import com.manish.shopnow.model.orders.OrderStatus
import com.manish.shopnow.model.orders.getOrderStatus
import com.manish.shopnow.util.hideBottomNavigationView

class OrderDetailFragment: Fragment(R.layout.fragment_order_detail) {
    private lateinit var binding: FragmentOrderDetailBinding
    private val billingProductsAdapter by lazy { BillingProductsAdapter() }
    private val args by navArgs<OrderDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderDetailBinding.bind(view)

        val order = args.order
        hideBottomNavigationView()

        setupOrderRv()

        binding.apply {

            tvOrderId.text = "Order #${order.orderId}"


            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status,
                )
            )

            val currentOrderState = when (getOrderStatus(order.orderStatus)) {
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered -> 3
                else -> 0
            }

            stepView.go(currentOrderState, false)
            if (currentOrderState == 3) {
                stepView.done(true)
            }

            tvFullName.text = order.address.fullName
            tvAddress.text = "${order.address.street} ${order.address.city}"
            tvPhoneNumber.text = order.address.phoneNo

            tvTotalPrice.text = "$ ${order.totalPrice}"

        }

        billingProductsAdapter.differ.submitList(order.products)
    }
    private fun setupOrderRv() {
        binding.rvProducts.apply {
            adapter = billingProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration())
        }
    }
}