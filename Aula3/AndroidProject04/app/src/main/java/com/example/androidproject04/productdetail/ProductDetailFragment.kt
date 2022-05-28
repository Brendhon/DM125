package com.example.androidproject04.productdetail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.androidproject04.R
import com.example.androidproject04.databinding.FragmentProductDetailBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

private const val TAG = "ProductDetailFragment"

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "Creating ProductDetailFragment")

        binding = FragmentProductDetailBinding.inflate(inflater)

        /* Inform that the owner of the cycle of life is himself */
        binding.lifecycleOwner = this

        /* Get remote config */
        val remoteConfig = Firebase.remoteConfig

        /* Show if remote config allow */
        setHasOptionsMenu(remoteConfig.getBoolean("delete_detail_view"))

        /* Allow show own menu */
        // setHasOptionsMenu(true)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /* Click event in menu */
        return when (item.itemId) {
            R.id.delete_product -> {
                binding.productDetailViewModel?.deleteProduct()
                findNavController().popBackStack() // Same action as user click back
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_details_menu, menu)
    }
}
