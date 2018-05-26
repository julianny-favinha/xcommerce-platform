package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.payment.api.PaymentInBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentInCreditCard
import com.mc851.xcommerce.clients.payment.api.PaymentOutBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentOutCreditCard
import com.mc851.xcommerce.clients.payment.api.StatusBoleto

interface PaymentClient {

    fun creditCardPayment(paymentIn: PaymentInCreditCard): PaymentOutCreditCard?

    fun boletoPayment(paymentIn: PaymentInBoleto): PaymentOutBoleto?

    fun statusBoletoPayment(code: String): StatusBoleto?

}