package com.mc920.xcommerce.clients.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.mc920.xcommerce.clients.product.api.ProductApi
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.UUID

class ProductClientOkHttp() : ProductClient {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = ObjectMapper()

    override fun listAllProducts(): List<ProductApi> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findProductById(id: UUID): ProductApi? {
        val httpUrl = HttpUrl.parse("https://ftt-catalog.herokuapp.com/")!!.newBuilder()
        httpUrl.addPathSegment(id.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body().toString(), ProductApi::class.java)
    }
}