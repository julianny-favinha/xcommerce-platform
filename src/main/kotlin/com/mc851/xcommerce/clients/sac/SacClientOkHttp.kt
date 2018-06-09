package com.mc851.xcommerce.clients.sac

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.SacClient
import com.mc851.xcommerce.clients.sac.api.CodeAPI
import com.mc851.xcommerce.clients.sac.api.MessageAPI
import com.mc851.xcommerce.clients.sac.api.TicketsAPI
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class SacClientOkHttp : SacClient {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val mediaType = MediaType.parse("application/json")

    private val siteId = "4753e50089193cefab74f76310e1fb40b2859307"
    private val siteUrl = "https://centralatendimento-mc857.azurewebsites.net/tickets/"

    override fun findTicketByUserId(userId: Long) : TicketsAPI?{
        val url = siteUrl + siteId + "/" + userId.toString()

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()

        if(!response.isSuccessful){
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun findTicketByCompraId(userId: Long, compraId: Long) : TicketsAPI?{
        val url = siteUrl + siteId + "/" + userId.toString() + "/compra/" + compraId.toString()

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun findTicketByTicketId(userId: Long, ticketId: Long) : TicketsAPI?{
        val url = siteUrl + siteId + "/" + userId.toString() + "/ticket/" + ticketId.toString()

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun addTicketToCompra(userId: Long, compraId: Long, message: MessageAPI) : CodeAPI?{
        val body = RequestBody.create(mediaType, objectMapper.writeValueAsString(message))

        val url = siteUrl + siteId + "/" + userId.toString() + "/compra/" + compraId.toString()
        val request = Request.Builder().url(url).post(body).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun addTicket(userId: Long, message: MessageAPI) : CodeAPI?{
        val body = RequestBody.create(mediaType, objectMapper.writeValueAsString(message))

        val url = siteUrl +  siteId + "/" + userId.toString()
        val request = Request.Builder().url(url).post(body).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun addMessageToTicket(userId: Long, ticketId: Long, message: MessageAPI) : TicketsAPI?{
        val body = RequestBody.create(mediaType, objectMapper.writeValueAsString(message))

        val url = siteUrl + siteId + "/" + userId.toString() + "/ticket/" + ticketId.toString()
        val request = Request.Builder().url(url).put(body).build()
        val response = okHttpClient.newCall(request).execute()

        if(!response.isSuccessful){
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun changeTicketCode(userId: Long, ticketId: Long, code: Int, message: MessageAPI) : CodeAPI? {
        val body = RequestBody.create(mediaType, objectMapper.writeValueAsString(message))

        val url = siteUrl + siteId + "/" + userId.toString() + "/ticket/" + ticketId.toString() + "?code=" + code
        val request = Request.Builder().url(url).delete(body).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }
}