package com.mc851.xcommerce.clients.payment.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class PaymentInCreditCard(val clientCardName: String,
                               val cpf: String,
                               val cardNumber: String,
                               val month: String,
                               val year: String,
                               val securityCode: String,
                               val value: String,
                               val instalments: String)

data class PaymentInBoleto(val clientName: String,
                           val cpf: String,
                           val address: String,
                           val cep: String,
                           val value: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentOutCreditCard(val result: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentOutBoleto(val code: String, val result: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class StatusBoleto(val status: String)