package com.example.fruittest.data.model

data class Fruit(
    val id: Int = 0,
    val name: String,
    val category: String,
    val price: Double,
    val quantity: Int,
    val expiryDate: String,
    val description: String
)
