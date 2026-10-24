package com.example.fruittest.data.model

data class Order(
    val id: Int = 0,
    val customerId: Int,
    val orderDate: String,
    val totalAmount: Double,
    val status: String
)
