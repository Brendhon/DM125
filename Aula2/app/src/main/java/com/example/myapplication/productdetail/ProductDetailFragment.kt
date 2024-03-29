package com.example.myapplication.productdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentProductDetailBinding

private const val TAG = "ProductDetailFragment"

class ProductDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "Creating ProductDetailFragment")

        val binding = FragmentProductDetailBinding.inflate(inflater)

        /* Inform that the owner of the cycle of life is himself */
        binding.lifecycleOwner = this

        // Fetch the product code and create the ViewModel here
        val productCode = ProductDetailFragmentArgs.fromBundle(requireArguments()).productCode

        /* Extract the code */
        val productDetailViewModelFactory = ProductDetailViewModelFactory(productCode)

        binding.productDetailViewModel = ViewModelProvider(
            this,
            productDetailViewModelFactory
        )[ProductDetailViewModel::class.java]

        return binding.root
    }
}
