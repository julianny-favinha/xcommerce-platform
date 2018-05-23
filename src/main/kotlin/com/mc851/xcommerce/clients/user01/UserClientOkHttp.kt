package com.mc851.xcommerce.clients.user01

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.UserClient
import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UpdateAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class UserClientOkHttp : UserClient {
    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val json = MediaType.parse("application/json")

    override fun register(registerAPI: RegisterAPI): String? {
        val body = RequestBody.create(json, objectMapper.writeValueAsString(registerAPI))

        val request = Request.Builder().url("http://us-central1-first-try-18f38.cloudfunctions.net/clientsAPI/register")
            .addHeader("api_key", "abc").post(body).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return response.body()?.string()
    }

    override fun getUserById(id: String): UserAPI? {
        val httpUrl =
            HttpUrl.parse("http://us-central1-first-try-18f38.cloudfunctions.net/clientsAPI/clients")!!.newBuilder()
        httpUrl.addPathSegment(id)

        val request = Request.Builder().url(httpUrl.build().toString()).addHeader("api_key", "abc").build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<UserAPI>(response.body()!!.byteStream())
    }

    override fun update(id: String, updateAPI: UpdateAPI) {
        val body = RequestBody.create(json, objectMapper.writeValueAsString(updateAPI))

        val request =
            Request.Builder().url("""http://us-central1-first-try-18f38.cloudfunctions.net/clientsAPI/update/$id""")
                .addHeader("api_key", "abc").put(body).build()

        okHttpClient.newCall(request).execute()
    }

}