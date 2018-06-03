package com.mc851.xcommerce.model.api

data class Orders(val orders: List<Order>)

data class Order(val products: List<Product>, val id: Long,)