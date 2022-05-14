package com.example.myapplication.network

data class Product(
    var id: Long = 0,
    var name: String,
    var description: String,
    var code: String,
    var price: Double,
    var quantity: String?,
)
