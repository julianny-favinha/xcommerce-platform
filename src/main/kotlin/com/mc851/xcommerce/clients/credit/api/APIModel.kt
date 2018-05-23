package com.mc851.xcommerce.clients.credit.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreditApi(val score: Int,
                     val document: String)

data class PaymentApi(val totalPaid: Long,
                      val totalValue: Long)