package com.mc851.xcommerce.clients.logistic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.LogisticClient
import com.mc851.xcommerce.clients.logistic.api.LogisticInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticOutAPI
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterOutApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackOutApi
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class LogisticClientOkHttp : LogisticClient {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val json = MediaType.parse("application/json")

    override fun calculateShipment(logisticPriceIn: LogisticInApi): LogisticOutAPI? {

        val httpUrl = HttpUrl.parse("https://hidden-basin-50728.herokuapp.com/calculafrete")!!.newBuilder()

        httpUrl.addQueryParameter("tipoEntrega", logisticPriceIn.shipType)
        httpUrl.addQueryParameter("cepOrigem", logisticPriceIn.cepFrom)
        httpUrl.addQueryParameter("cepDestino", logisticPriceIn.cepDst)
        httpUrl.addQueryParameter("peso", logisticPriceIn.packWeight.toString())
        httpUrl.addQueryParameter("tipoPacote", logisticPriceIn.packType)
        httpUrl.addQueryParameter("comprimento", logisticPriceIn.packLen.toString())
        httpUrl.addQueryParameter("altura", logisticPriceIn.packHeight.toString())
        httpUrl.addQueryParameter("largura", logisticPriceIn.packWidth.toString())

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun register(logisticRegisterIn: LogisticRegisterInApi): LogisticRegisterOutApi? {

        val body = RequestBody.create(json, objectMapper.writeValueAsString(logisticRegisterIn))

        val request =
            Request.Builder().url("https://hidden-basin-50728.herokuapp.com/cadastrarentrega").post(body).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun trackOrder(logisticTrackIn: LogisticTrackInApi): LogisticTrackOutApi? {

        val httpUrl = HttpUrl.parse("https://hidden-basin-50728.herokuapp.com/rastrearentrega")!!.newBuilder()

        httpUrl.addPathSegment(logisticTrackIn.trackCode)
        httpUrl.addQueryParameter("apiKey", logisticTrackIn.apiKey)

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

}