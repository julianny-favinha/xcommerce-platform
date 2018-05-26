package com.mc851.xcommerce.clients.payment.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class PaymentInCreditCard(val clientCardName: String,
                               val cpf: String,
                               val cardNumber: String,
                               val month: String,
                               val year: String,
                               val securityCode: String,
                               val value: String,
                               val installments: String)

data class PaymentInBoleto(val clientName: String,
                           val cpf: String,
                           val address: String,
                           val cep: String,
                           val value: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentOutCreditCard(val operationHash: String, val result: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentOutBoleto(val errorMessage: String?, val code: String, val documentRep: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class StatusBoleto(val status: String)