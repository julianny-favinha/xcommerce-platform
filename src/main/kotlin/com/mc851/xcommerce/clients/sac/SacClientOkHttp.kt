package com.mc851.xcommerce.clients.sac

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.SacClient
import com.mc851.xcommerce.clients.sac.api.CodeAPI
import com.mc851.xcommerce.clients.sac.api.MessageAPI
import com.mc851.xcommerce.clients.sac.api.TicketsAPI
import mu.KotlinLogging
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class SacClientOkHttp : SacClient {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val log = KotlinLogging.logger {}


    private val mediaType = MediaType.parse("application/json")
    private val siteId = "4753e50089193cefab74f76310e1fb40b2859307"
    private val siteUrl = "https://centralatendimento-mc857.azurewebsites.net/tickets/"

    companion object {
        fun add_mark(userId: Long): String = userId.toString() + "creditado"
    }

    override fun findTicketByUserId(userId: Long) : TicketsAPI?{
        val url = siteUrl + siteId + "/" + add_mark(userId)

        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()

        if(!response.isSuccessful){
            return null
        }

        val body = response.body()
        log.debug { body }
        return objectMapper.readValue(body!!.byteStream())
    }

    override fun findTicketByCompraId(userId: Long, compraId: Long) : TicketsAPI?{
        val url = siteUrl + siteId + "/" + add_mark(userId) + "/compra/" + compraId.toString()

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
        val json = objectMapper.writeValueAsString(message)
        log.info { json }
        val body = RequestBody.create(mediaType, json)

        val url = siteUrl + siteId + "/" + add_mark(userId) + "/compra/" + compraId.toString()
        log.info { url }

        val request = Request.Builder().url(url).post(body).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun addTicket(userId: Long, message: MessageAPI) : CodeAPI?{
        val json = objectMapper.writeValueAsString(message)
        log.info { json }
        val body = RequestBody.create(mediaType, json)

        val url = siteUrl +  siteId + "/" + add_mark(userId)
        log.info { url }
        val request = Request.Builder().url(url).post(body).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun addMessageToTicket(userId: Long, ticketId: Long, message: MessageAPI) : TicketsAPI?{
        val json = objectMapper.writeValueAsString(message)
        log.info { json }
        val body = RequestBody.create(mediaType, json)

        val url = siteUrl + siteId + "/" + add_mark(userId) + "/ticket/" + ticketId.toString()
        log.info { url }
        val request = Request.Builder().url(url).put(body).build()
        val response = okHttpClient.newCall(request).execute()

        if(!response.isSuccessful){
            return null
        }

        return objectMapper.readValue(response.body()!!.byteStream())
    }

    override fun changeTicketCode(userId: Long, ticketId: Long, code: Int, message: MessageAPI) : CodeAPI? {
        val body = RequestBody.create(mediaType, objectMapper.writeValueAsString(message))

        val url = siteUrl + siteId + "/" + add_mark(userId) + "/ticket/" + ticketId.toString() + "?code=" + code
        val request = Request.Builder().url(url).delete(body).build()
        val response = okHttpClient.newCall(request).execute()

        return objectMapper.readValue(response.body()!!.byteStream())
    }
}