package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.CreditClient
import com.mc851.xcommerce.model.Credit
import org.springframework.stereotype.Service

@Service
class CreditService(val creditClient: CreditClient) {
    fun validateUser() {
        creditClient.getScoreByCpf()
    }
}