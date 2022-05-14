package com.example.myapplication.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.network.Product
import com.example.myapplication.network.SalesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "ProductListViewModel"

class ProductListViewModel : ViewModel() {
    private var viewModelJob = Job() // Initiate a new jog

    // Declare the new scope
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /* Declare MutableLiveData */
    private val _products = MutableLiveData<List<Product>>()

    /* Observable to listening changes on _products */
    val products: LiveData<List<Product>> get() = _products

    /* When create ProductListViewModel,  get the products */
    init {
        getProducts()
    }

    /* Get products */
    private fun getProducts() {
        Log.i(TAG, "Preparing to request the product list")

        /* Got to a new tread */
        coroutineScope.launch {

            /* Get promise */
            val getProductsDeferred = SalesApi.retrofitService.getProducts()
            Log.i(TAG, "Fetching products")

            /* Await the response */
            val productsList = getProductsDeferred.await()
            Log.i(TAG, "Number of products: ${productsList.size}")

            /* Set value */
            _products.value = productsList
        }

        Log.i(TAG, "Product list request")
    }

    fun refreshProducts() {
        Log.i(TAG, "Refresh Products")
        getProducts()
    }

    /* Clear and close JOB */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}