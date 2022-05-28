package com.example.androidproject04.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidproject04.persistence.Product
import com.example.androidproject04.persistence.ProductRepository

private const val TAG = "ProductListViewModel"

class ProductListViewModel : ViewModel() {

    /* Declare MutableLiveData */
    private lateinit var _products: MutableLiveData<List<Product>>

    /* Observable to listening changes on _products */
    val products: LiveData<List<Product>> get() = _products

    /* When create ProductListViewModel,  get the products */
    init {
        getProducts()
    }

    /* Get products */
    private fun getProducts() {
        _products = ProductRepository.getProducts();
    }

    /* Clear and close JOB */
    override fun onCleared() {
        super.onCleared()
    }
}