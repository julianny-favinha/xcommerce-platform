package com.mc851.xcommerce.clients.payment

import com.mc851.xcommerce.clients.payment.api.PaymentInBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentInCreditCard
import com.mc851.xcommerce.clients.payment.api.PaymentOutBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentOutCreditCard
import com.mc851.xcommerce.clients.payment.api.StatusBoleto

interface PaymentClient {

    fun creditCardPayment(request: PaymentInCreditCard): PaymentOutCreditCard?

    fun boletoPayment(request: PaymentInBoleto): PaymentOutBoleto?

    fun statusBoletoPayment(code: String): StatusBoleto?

}