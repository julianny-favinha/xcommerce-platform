package com.mc851.xcommerce.clients.logistic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.LogisticClient
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.UUID

class LogisticClientOkHttp : LogisticClient {
    private val okHttpClient = OkHttpClient()

    private val objectMapper = jacksonObjectMapper()

    override fun calculateShipment(
            shipType: String,
            cepFrom: String,
            cepDst: String,
            packWeight: Int,
            packType: String,
            packLen: Double,
            packHeight: Double,
            packWidth: Double): Integer {

        val httpUrl = HttpUrl.parse("https://hidden-basin-50728.herokuapp.com/calculafrete")!!.newBuilder()

        httpUrl.addQueryParameter("tipoEntrega", shipType)
        httpUrl.addQueryParameter("cepOrigem", cepFrom)
        httpUrl.addQueryParameter("cepDestino", cepDst)
        httpUrl.addQueryParameter("peso", packWeight.toString())
        httpUrl.addQueryParameter("tipoPacote", packType)
        httpUrl.addQueryParameter("comprimento", packLen.toString())
        httpUrl.addQueryParameter("altura", packHeight.toString())
        httpUrl.addQueryParameter("largura", packWidth.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<LogisticApi>(response.body()!!.byteStream())
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