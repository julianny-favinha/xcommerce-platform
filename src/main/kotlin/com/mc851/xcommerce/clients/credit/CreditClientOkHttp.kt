package com.mc851.xcommerce.clients.credit

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.CreditClient
import com.mc851.xcommerce.clients.credit.api.CreditApi
import com.mc851.xcommerce.clients.credit.api.PaymentApi
import okhttp3.*

class CreditClientOkHttp : CreditClient {
    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val json = MediaType.parse("application/json")

    override fun getScoreByCpf(cpf: String): CreditApi? {
        val httpUrl = HttpUrl.parse("https://glacial-brook-98386.herokuapp.com/score")!!.newBuilder()
        httpUrl.addPathSegment(cpf)

        val request = Request.Builder().url(httpUrl.build().toString())
                .addHeader("x-api-key", "tmvcgvc").build()

        val response = okHttpClient.newCall(request).execute()

        System.out.println(response)

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<CreditApi>(response.body()!!.byteStream())

    }

    override fun payment(cpf: String, paymentApi: PaymentApi) {
        val httpUrl = HttpUrl.parse("https://glacial-brook-98386.herokuapp.com/")!!.newBuilder()
        httpUrl.addQueryParameter("payment", cpf)

        val body = RequestBody.create(json, objectMapper.writeValueAsString(paymentApi))


        val request = Request.Builder().url(httpUrl.build().toString()).post(body)
                .addHeader("x-api-key", "tmvcgvc").build()

        okHttpClient.newCall(request).execute()
    }
}