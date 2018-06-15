package com.mc851.xcommerce.clients.product01

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.ProductClient
import com.mc851.xcommerce.clients.product01.api.CategoryApi
import com.mc851.xcommerce.clients.product01.api.ProductApi
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.UUID

class ProductClientOkHttp : ProductClient {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    override fun listAllProducts(highlight: Boolean): List<ProductApi> {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/products/group/2d535b83-7b2a-48f7-9e3e-08b3a0b9bf12")!!.newBuilder()
        httpUrl.addQueryParameter("highlight", highlight.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return emptyList()
        }

        return objectMapper.readValue<List<ProductApi>>(response.body()!!.byteStream())
    }

    override fun findProductById(id: UUID): ProductApi? {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/products")!!.newBuilder()
        httpUrl.addPathSegment(id.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<ProductApi>(response.body()!!.byteStream())
    }

    override fun listAllCategories(): List<CategoryApi> {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/categories/group/2d535b83-7b2a-48f7-9e3e-08b3a0b9bf12")!!.newBuilder()

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return emptyList()
        }

        return objectMapper.readValue<List<CategoryApi>>(response.body()!!.byteStream())
    }

    override fun findCategoryById(id: UUID): CategoryApi? {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/categories")!!.newBuilder()
        httpUrl.addPathSegment(id.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<CategoryApi>(response.body()!!.byteStream())
    }

    override fun search(text: String): List<ProductApi> {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/products/search")!!.newBuilder()
        httpUrl.addPathSegment(text)
        System.out.println(httpUrl.toString())
        val request = Request.Builder().url(httpUrl.build().toString()).build()
        System.out.println(request.toString())
        val response = okHttpClient.newCall(request).execute()
        System.out.println(response.toString())
        if (!response.isSuccessful) {
            return emptyList()
        }

        return objectMapper.readValue<List<ProductApi>>(response.body()!!.byteStream())
    }

    override fun release(id: UUID, quantity: Long): Boolean {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/reservation/release")!!.newBuilder()
        httpUrl.addPathSegment(id.toString())

        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), quantity.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).put(requestBody).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return false
        }

        return true
    }

    override fun reserve(id: UUID, quantity: Long): Boolean {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/reservation/reserve")!!.newBuilder()
        httpUrl.addPathSegment(id.toString())

        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), quantity.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).put(requestBody).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return false
        }

        return true
    }
}