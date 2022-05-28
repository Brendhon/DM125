package com.example.androidproject04.persistence

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

/* If exist new attr on Firestore and in the app maybe not exist */
@IgnoreExtraProperties
data class Product(
    @Exclude var id: String? = null,
    var userId: String? = null,
    var name: String? = null,
    var description: String? = null,
    var code: String? = null,
    var price: Double? = null
)
