package com.mc851.xcommerce.clients.address

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.address.api.*
import com.mc851.xcommerce.clients.product01.api.ProductApi
import okhttp3.*

class AddressClientOkHttp : AddressClient {

    private val xapikey = "f676ef30-eec0-4116-8724-81cc102f107b"
    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    override fun findByCep(cep: String): AddressApi? {
        val httpUrl = HttpUrl.parse("https://node.thiagoelg.com/paises/br/cep/")!!.newBuilder()
        httpUrl.addPathSegment(cep)

        val request = Request.Builder().url(httpUrl.build().toString()).addHeader("x-api-key", xapikey).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun insertCep(cep: String, logradouro: String, bairro: String, idCidade: String): CepApi? {
        val httpUrl = HttpUrl.parse("https://node.thiagoelg.com/paises/br/cep/")!!.newBuilder()

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cep", cep)
                .addFormDataPart("logradouro", logradouro)
                .addFormDataPart("bairro", bairro)
                .addFormDataPart("idCidade", idCidade)
                .build()

        val request = Request.Builder().url(httpUrl.build().toString()).post(requestBody).addHeader("x-api-key", xapikey).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun listCities(uf: String, query: String): List<CityApi> {
        val httpUrl = HttpUrl.parse("https://node.thiagoelg.com/paises/br/estados/$uf/cidades/")!!.newBuilder()
        httpUrl.addPathSegment(query)

        val request = Request.Builder().url(httpUrl.build().toString()).addHeader("x-api-key", xapikey).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return emptyList()
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun insertCity(uf: String, nome: String): IdApi? {
        val httpUrl = HttpUrl.parse("https://node.thiagoelg.com/paises/br/estados/$uf/cidades")!!.newBuilder()

        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), nome)

        val request = Request.Builder().url(httpUrl.build().toString()).post(requestBody).addHeader("x-api-key", xapikey).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun listStates(): List<StateApi> {
        val httpUrl = HttpUrl.parse("https://node.thiagoelg.com/paises/br/estados/")!!.newBuilder()

        val request = Request.Builder().url(httpUrl.build().toString()).addHeader("x-api-key", xapikey).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return emptyList()
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun insertState(uf: String, nome: String): IdApi? {
        val httpUrl = HttpUrl.parse("https://node.thiagoelg.com/paises/br/estados/")!!.newBuilder()

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uf", uf)
                .addFormDataPart("nome", nome)
                .build()

        val request = Request.Builder().url(httpUrl.build().toString()).post(requestBody).addHeader("x-api-key", xapikey).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

}