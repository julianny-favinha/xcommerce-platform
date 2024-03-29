package com.mc851.xcommerce.service.user

import com.mc851.xcommerce.clients.CreditClient
import com.mc851.xcommerce.clients.credit.api.PaymentApi
import com.mc851.xcommerce.model.api.User

class CreditService(private val creditClient: CreditClient) {
    fun validateUser(cpf: String, value: Long): Boolean {
        val (score, _) = creditClient.getScoreByCpf(cpf) ?: return true
        System.out.println(score.toString())
        if (value < 1000L) {
            return when (score) {
                in 0..249 -> true
                in 250..499 -> true
                in 500..749 -> true
                in 750..1000 -> true
                else -> false
            }
        } else {
            return when (score) {
                in 0..249 -> false
                in 250..499 -> false
                in 500..749 -> true
                in 750..1000 -> true
                else -> false
            }
        }
    }

    fun payment(user: User, totalPaid: Long) {
        creditClient.payment(user.cpf, PaymentApi(totalPaid, totalPaid))
    }
}