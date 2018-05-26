package com.mc851.xcommerce.model.api

data class CheckoutIn(val products: List<Product>)

data class CheckoutOut(val status: Long)