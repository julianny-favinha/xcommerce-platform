package com.mc851.xcommerce.clients.product01

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.product01.api.CategoryApi
import com.mc851.xcommerce.clients.product01.api.ProductApi
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.UUID

class ProductClientOkHttp : ProductClient {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    override fun listAllProducts(highlight: Boolean): List<ProductApi> {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/products")!!.newBuilder()
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
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/categories")!!.newBuilder()

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
}