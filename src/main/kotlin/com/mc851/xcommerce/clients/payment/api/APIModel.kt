package com.mc851.xcommerce.clients.payment.api

data class PaymentInCreditCard(val clientCardName: String,
                               val cpf: String,
                               val cardNumber: String,
                               val month: String,
                               val year: String,
                               val securityCode: String,
                               val value: String,
                               val installments: String)

data class PaymentOutCreditCard(val errorMessage: String?, val operationHash: String, val result: String)

data class PaymentInBoleto(val clientName: String,
                           val cpf: String,
                           val address: String,
                           val cep: String,
                           val value: String)

data class PaymentOutBoleto(val errorMessage: String?, val code: String, val documentRep: String)

data class StatusBoleto(val status: String)