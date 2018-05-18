package com.mc851.xcommerce.clients

interface CreditClient {
    fun getScoreByCpf(cpf: String): Int

    fun payment(cpf: String, totalPaid: Long, totalValue: Long)
}