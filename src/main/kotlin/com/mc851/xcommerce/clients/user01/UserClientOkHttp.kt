package com.mc851.xcommerce.clients.user01

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mc851.xcommerce.clients.UserClient
import com.mc851.xcommerce.clients.user01.api.RegisterAPI
import com.mc851.xcommerce.clients.user01.api.UserAPI
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class UserClientOkHttp : UserClient {
    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    override fun register(registerAPI: RegisterAPI): String? {
        val httpUrl =
            HttpUrl.parse("http://us-central1-first-try-18f38.cloudfunctions.net/clientsAPI/register")!!.newBuilder()

        val body = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(registerAPI))

        val request = Request.Builder().url(httpUrl.build().toString()).post(body).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<String>(response.body()!!.byteStream())

    }

    override fun getUserById(id: String): UserAPI? {
        val httpUrl =
            HttpUrl.parse("http://us-central1-first-try-18f38.cloudfunctions.net/clientsAPI/clients")!!.newBuilder()
        httpUrl.addPathSegment(id)

        val request = Request.Builder().url(httpUrl.build().toString()).build()

        val response = okHttpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            return null
        }

        return objectMapper.readValue<UserAPI>(response.body()!!.byteStream())
    }

    //
    //    override fun changePassword(id: UUID, password: String, samePass: String): Boolean {
    //        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    //        return true
    //    }
    //
    //    override fun addAddress(id: UUID, cep: String, address: String): Boolean {
    //        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    //        return true
    //    }
    //
    //    // TODO: Como são feitos os requests aqui?
    //    // Diferente dos já feitos, aqui são gets, não parsers
    //    // Possivelmente: https://github.com/square/okhttp/wiki/Recipes
    //    // em suma, montar um corpo e mandar
    //    override fun updateCliente(
    //            id: UUID,
    //            name: String?,
    //            email: String?,
    //            password: String?,
    //            samePass: String?,
    //            birthDate: String?,
    //            cpf: String?,
    //            gender: String?,
    //            telephone: String?
    //    ): String {
    //        TODO()
    //    }
    //
    //    override fun signUp(
    //            name: String,
    //            email: String,
    //            password: String,
    //            samePass: String,
    //            birthDate: String?,
    //            cpf: String,
    //            gender: String?,
    //            telephone: String?
    //    ): String {
    //        TODO()
    //    }
    //
    //    override fun getCliente(id: UUID): User {
    //        TODO()
    //    }

}