package com.manish.shopnow.fragments.shopping

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.manish.shopnow.R
import com.manish.shopnow.adapters.AddressAdapter
import com.manish.shopnow.adapters.BillingProductsAdapter
import com.manish.shopnow.databinding.FragmentBillingBinding
import com.manish.shopnow.model.Address
import com.manish.shopnow.model.CartProduct
import com.manish.shopnow.model.orders.Order
import com.manish.shopnow.model.orders.OrderStatus
import com.manish.shopnow.util.HorizontalItemDecoration
import com.manish.shopnow.util.Resource
import com.manish.shopnow.viewmodel.BillingViewModel
import com.manish.shopnow.viewmodel.OrderViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONObject


@AndroidEntryPoint
class BillingFragment : Fragment(R.layout.fragment_billing), PaymentResultListener {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductsAdapter by lazy { BillingProductsAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var totalPrice = 0f


    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products = args.cartProducts.toList()
        totalPrice = args.totalPrice

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBillingBinding.bind(view)

        setupBillingProductsRv()
        setupAddressRv()
        if (!args.payment) {
            binding.apply {
                buttonPlaceOrder.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE
                paymentBox.visibility = View.INVISIBLE
            }
        }

        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }

        lifecycleScope.launchWhenStarted {
            billingViewModel.address.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            orderViewModel.order.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp() // this is used to go back to previous activity if result is success
                        Snackbar.make(requireView(), "Your order is placed", Snackbar.LENGTH_LONG)
                            .show()
                    }

                    is Resource.Error -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
        billingProductsAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = "$ ${totalPrice}"

        addressAdapter.onClick = {
            selectedAddress = it
            if (!args.payment) {
                val b = Bundle().apply { putParcelable("address", selectedAddress) }
                findNavController().navigate(R.id.action_billingFragment_to_addressFragment, b)
            }
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "Please select an address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            setUpPayment()
            orderViewModel.placeOrder(Order(
                OrderStatus.Ordered.status,
                totalPrice,
                products,
                selectedAddress!!
            ))
            Snackbar.make(requireView(), "Your order is placed", Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun setUpPayment() {
        Checkout.preload(requireContext())
        val co = Checkout()
        co.setKeyID("rzp_test_7U4EsZapSlrVL7")

        try {
            val options = JSONObject()
            options.put("name","ShopNow")
            options.put("description","Best E Commerce App")
            //You can omit the image option to fetch the image from the dashboard
//            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#FFBB86FC");
            options.put("currency","INR");
            options.put("amount","${totalPrice*100}")//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email","shakyamanish602@gmail.com")
            prefill.put("contact","7307338577")

            options.put("prefill",prefill)
            co.open(requireActivity(),options)
        }catch (e: Exception){
            Toast.makeText(requireContext(),"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    private fun showOrderConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                val order = Order(
                    OrderStatus.Ordered.status,
                    totalPrice,
                    products,
                    selectedAddress!!
                )
                orderViewModel.placeOrder(order)
                dialog.dismiss()
            }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupAddressRv() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductsRv() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingProductsAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(requireContext(), "Payment Failed", Toast.LENGTH_SHORT).show()

    }

}