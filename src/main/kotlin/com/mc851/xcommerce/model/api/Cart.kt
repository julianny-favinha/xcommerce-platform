package com.mc851.xcommerce.model.api

data class Cart(val cartItems: List<CartItem>)

data class CartItem(val product: Product, val quantity: Long)