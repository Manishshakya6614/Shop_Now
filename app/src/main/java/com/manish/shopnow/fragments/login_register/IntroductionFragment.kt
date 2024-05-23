package com.manish.shopnow.fragments.login_register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.manish.shopnow.R
import com.manish.shopnow.databinding.FragmentIntroductionBinding

class IntroductionFragment: Fragment(R.layout.fragment_introduction) {

    private lateinit var binding: FragmentIntroductionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroductionBinding.bind(view)

        binding.buttonLetsGo.setOnClickListener {
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }
    }
}