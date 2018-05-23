package com.mc851.xcommerce.clients.payment

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.payment.api.PaymentInBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentInCreditCard
import com.mc851.xcommerce.clients.payment.api.PaymentOutBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentOutCreditCard
import com.mc851.xcommerce.clients.payment.api.StatusBoleto
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class PaymentClientOkHttp : PaymentClient {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    override fun creditCardPayment(paymentIn: PaymentInCreditCard): PaymentOutCreditCard? {
        val httpUrl = HttpUrl.parse("https://payment-server-mc851.herokuapp.com/payments/creditCard")!!.newBuilder()

        val jsonType = MediaType.parse("application/json")
        val requestBody = RequestBody.create(jsonType, objectMapper.writeValueAsString(paymentIn))

        val request = Request.Builder().post(requestBody).url(httpUrl.build().toString()).build()
        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<PaymentOutCreditCard>(response.body()!!.byteStream())
    }

    override fun boletoPayment(paymentIn: PaymentInBoleto): PaymentOutBoleto? {
        val httpUrl = HttpUrl.parse("https://payment-server-mc851.herokuapp.com/payments/bankTicket")!!.newBuilder()

        val jsonType = MediaType.parse("application/json")
        val requestBody = RequestBody.create(jsonType, objectMapper.writeValueAsString(paymentIn))

        val request = Request.Builder().post(requestBody).url(httpUrl.build().toString()).build()
        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<PaymentOutBoleto>(response.body()!!.byteStream())
    }

    override fun statusBoletoPayment(code: String): StatusBoleto? {
        val httpUrl = HttpUrl.parse("https://payment-server-mc851.herokuapp.com/bankTicket")!!.newBuilder()
        httpUrl.addPathSegment(code)
        httpUrl.addPathSegment("/status")

        val request = Request.Builder().url(httpUrl.build().toString()).build()
        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<StatusBoleto>(response.body()!!.byteStream())
    }
}