package com.mc851.xcommerce.controller

import com.mc851.xcommerce.model.Categories
import com.mc851.xcommerce.service.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("category")
class CategoryController {

    @Autowired
    lateinit var categoryService: CategoryService

    @GetMapping("/")
    fun getAll(): ResponseEntity<Categories> {
        val categories = categoryService.getAll()
        return handleResponse(categories)
    }

}
