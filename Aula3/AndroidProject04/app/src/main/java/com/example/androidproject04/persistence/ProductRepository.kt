package com.example.androidproject04.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "ProductRepository"
private const val COLLECTION = "products"
private const val FIELD_USER_ID = "userId"
private const val FIELD_NAME = "name"
private const val FIELD_DESCRIPTION = "description"
private const val FIELD_CODE = "code"
private const val FIELD_PRICE = "price"

object ProductRepository {
    /* Get auth user */
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    /* Get a firebase instance */
    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    /* Save or Edit document */
    fun saveProduct(product: Product): String {
        /* product.id != null - User already exist  */
        val document = if (product.id != null) {
            /* Get document ref */
            firebaseFirestore.collection(COLLECTION).document(product.id!!)
        } else {
            /* If is new doc - Added uid in the new obj */
            product.userId = firebaseAuth.currentUser!!.uid

            /* get Collection ref */
            firebaseFirestore.collection(COLLECTION).document()
        }

        /* Save doc */
        document.set(product)

        /* Return the doc id */
        return document.id
    }

    /* Delete document */
    fun deleteProduct(productId: String) {
        /* get document ref */
        val document = firebaseFirestore.collection(COLLECTION).document(productId)

        /* Delete */
        document.delete()
    }

    /* Get product by code */
    fun getProductByCode(code: String): MutableLiveData<Product> {

        /* Declare the observer that will be returned */
        val liveProduct: MutableLiveData<Product> = MutableLiveData()

        /* Fetch data on Firestore */
        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_CODE, code) // Get when code is the specific code
            .whereEqualTo(
                FIELD_USER_ID,
                firebaseAuth.currentUser!!.uid
            ) // Only get data that the user is the owner
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                /* querySnapshot - result */
                /* firebaseFirestoreException - error if find */

                /* Verify if find some error */
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                /* Initiate the product list */
                val products = ArrayList<Product>()

                /* Verify if exist */
                if (querySnapshot != null && !querySnapshot.isEmpty) {


                    /* List the result */
                    querySnapshot.forEach {
                        // Parse the value to Product
                        val product = it.toObject<Product>()

                        // Get the doc id
                        product.id = it.id

                        // Added product on the list
                        products.add(product)
                    }

                } else {

                    /* If no products are found */
                    Log.d(TAG, "No product has been found")
                }

                /* Publishing values to all listeners  */
                liveProduct.postValue(products[0])
            }
        return liveProduct
    }

    /* Get products */
    fun getProducts(): MutableLiveData<List<Product>> {

        /* Declare the observer that will be returned */
        val liveProducts = MutableLiveData<List<Product>>()

        /* Fetch data on Firestore */
        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(
                FIELD_USER_ID,
                firebaseAuth.currentUser!!.uid
            ) // Only get data that the user is the owner
            .orderBy(FIELD_NAME, Query.Direction.ASCENDING) // Order by ASCENDING
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                /* querySnapshot - result */
                /* firebaseFirestoreException - error if find */

                /* Verify if find some error */
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener // Specify that return is to addSnapshotListener function
                }

                /* Initiate the product list */
                val products = ArrayList<Product>()

                /* Verify if exist */
                if (querySnapshot != null && !querySnapshot.isEmpty) {

                    querySnapshot.forEach {
                        // Parse the value to Product
                        val product = it.toObject<Product>()

                        // Get the doc id
                        product.id = it.id

                        // Added product on the list
                        products.add(product)
                    }

                } else {
                    /* If no products are found */
                    Log.d(TAG, "No product has been found")
                }

                /* Publishing values to all listeners  */
                liveProducts.postValue(products)
            }

        /* Return the product observer */
        return liveProducts
    }
}