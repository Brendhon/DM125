package com.example.myapplication.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.myapplication.databinding.FragmentProductsListBinding

private const val TAG = "ProductsListFragment"

class ProductsListFragment : Fragment() {
    private val productListViewModel: ProductListViewModel by lazy {
        /* Associate the view model (ProductListViewModel) with this Fragment */
        ViewModelProvider(this)[ProductListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProductsListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.productListViewModel = productListViewModel

        /* Show line to divide */
        val itemDecor = DividerItemDecoration(context, VERTICAL)
        binding.rcvProducts.addItemDecoration(itemDecor);

        /* Get an event when clicking on a product */
        binding.rcvProducts.adapter =
            ProductAdapter(ProductAdapter.ProductClickListener { product ->
                Log.i(TAG, "Product selected: ${product.code}")

                /* Navigate to next page and pass code */
                this.findNavController()
                    .navigate(ProductsListFragmentDirections.actionShowProductDetail(product.code))
            })

        /* Refresh data */
        binding.productsRefresh.setOnRefreshListener {
            Log.i(TAG, "Refreshing products list")
            productListViewModel.refreshProducts()
            binding.productsRefresh.isRefreshing = false
        }

        return binding.root
    }

}
