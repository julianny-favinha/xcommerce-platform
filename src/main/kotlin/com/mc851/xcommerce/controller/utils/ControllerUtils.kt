package com.mc851.xcommerce.controller.utils

import org.springframework.http.ResponseEntity

fun <T> handleResponse(body: T?): ResponseEntity<T> {
    return body?.let { ResponseEntity.ok(it) } ?: return ResponseEntity.notFound().build()
}

fun <T> handleErrorResponse(body: T?): ResponseEntity<T> {
    return body?.let { ResponseEntity.ok(it) } ?: return ResponseEntity.badRequest().build()
}