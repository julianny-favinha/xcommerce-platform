package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.credit.api.CreditApi
import com.mc851.xcommerce.clients.credit.api.PaymentApi

interface CreditClient {
    fun getScoreByCpf(cpf: String): CreditApi?

    fun payment(cpf: String, paymentApi: PaymentApi)
}