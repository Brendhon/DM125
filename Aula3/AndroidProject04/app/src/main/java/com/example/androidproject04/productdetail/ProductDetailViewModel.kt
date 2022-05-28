package com.example.androidproject04.productdetail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidproject04.persistence.Product
import com.example.androidproject04.persistence.ProductRepository

private const val TAG = "ProductDetailViewModel"

class ProductDetailViewModel(private val code: String?) : ViewModel() {

    /* Declare MutableLiveData */
    lateinit var product: MutableLiveData<Product>

    /* When create ProductListViewModel,  get the products */
    init {
        if (code != null) {
            getProduct(code)
        } else {
            product = MutableLiveData<Product>()
            product.value = Product()
        }
    }

    private fun getProduct(productCode: String) {
        product = ProductRepository.getProductByCode(productCode)
    }

    /* Delete product */
    @SuppressLint("NullSafeMutableLiveData")
    fun deleteProduct() {
        /*Verify if exist */
        if (product.value?.id != null) {
            ProductRepository.deleteProduct(product.value!!.id!!)
            product.value = null
        }
    }

    /* Save the product and clear */
    override fun onCleared() {
        if (product.value != null
            && product.value!!.code != null
            && product.value!!.code!!.isNotBlank()
        ) {
            // Save|Edit the product
            ProductRepository.saveProduct(product.value!!)
        }
        super.onCleared()
    }

}