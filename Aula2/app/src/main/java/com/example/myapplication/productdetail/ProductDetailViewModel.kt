package com.example.myapplication.productdetail

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

private const val TAG = "ProductDetailViewModel"

class ProductDetailViewModel(private val code: String) : ViewModel() {
    private var viewModelJob = Job() // Initiate a new jog

    // Declare the new scope
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /* Declare MutableLiveData */
    private val _product = MutableLiveData<Product>()

    /* Observable to listening changes on _product */
    val products: LiveData<Product> get() = _product

    /* When create ProductListViewModel,  get the products */
    init {
        getProduct()
    }

    private fun getProduct() {
        Log.i(TAG, "Preparing to request a product by its code")
        coroutineScope.launch {
            _product.value = SalesApi.retrofitService.getProductByCode(code).await()
        }
        Log.i(TAG, "Product requested by code")
    }

    /* Clear and close JOB */
    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared")
        viewModelJob.cancel()
    }

}