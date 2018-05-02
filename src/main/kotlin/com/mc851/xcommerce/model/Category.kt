package com.mc851.xcommerce.model

data class Category(val id: Long, val name: String)

data class Categories(val categories: List<Category>)