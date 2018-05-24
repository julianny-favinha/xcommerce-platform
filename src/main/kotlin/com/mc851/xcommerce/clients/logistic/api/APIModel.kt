package com.mc851.xcommerce.clients.logistic.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


data class LogisticPriceInApi(val shipType: String,
                              val cepDst: String,
                              val packWeight: Long,
                              val packType: String,
                              val packLen: Double,
                              val packHeight: Double,
                              val packWidth: Double) {
    val cepFrom = "13465-450"
}

data class LogisticPriceOutAPI(val preco: Int)


data class History(val hora: String, val local: String, val mensagem: String)

data class LogisticTrackInApi(val trackCode: String, val apiKey: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LogisticTrackOutApi(     val status:  String,
                                    val idProduto: String?,
                                    val tipoEntrega: String,
                                    val preco: Int,
                                    val cepOrigem: String,
                                    val cepDestino: String,
                                    val peso: Double,
                                    val tipoPacote: String,
                                    val altura: Double,
                                    val largura: Double,
                                    val comprimento: Double,
                                    val historicoRastreio: List<History>)


@JsonIgnoreProperties(ignoreUnknown = true)
data class LogisticRegisterInApi(    val idProduto: Int,
                                     val tipoEntrega: String,
                                     val cepOrigem: String,
                                     val cepDestino: String,
                                     val peso: Double,
                                     val tipoPacote: String,
                                     val altura: Double,
                                     val largura: Double,
                                     val comprimento: Double) {
    val apiKey = "b09f7e40-36a7-5c00-9d62-84112b847952"
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class LogisticRegisterOutApi(  val status: String, val codigoRastreio: String)
