package com.mc851.xcommerce.controller

import org.springframework.http.ResponseEntity

fun <T> handleResponse(body: T?): ResponseEntity<T> {
    return body?.let { ResponseEntity.ok(it) } ?: return ResponseEntity.notFound().build()
}