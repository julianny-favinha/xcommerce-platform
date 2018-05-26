package com.mc851.xcommerce.service.payment

import com.mc851.xcommerce.clients.PaymentClient
import com.mc851.xcommerce.clients.payment.api.PaymentInBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentInCreditCard
import com.mc851.xcommerce.clients.payment.api.StatusBoleto
import com.mc851.xcommerce.model.internal.BoletoPayment
import com.mc851.xcommerce.model.internal.CreditCardPayment
import com.mc851.xcommerce.model.internal.PaymentResult
import mu.KotlinLogging

class PaymentService(private val paymentClient: PaymentClient) {

    private val logger = KotlinLogging.logger {}

    fun payBoleto(boletoPayment: BoletoPayment): PaymentResult {

        val userInfo = boletoPayment.userInfo

        val paymentIn = PaymentInBoleto(userInfo.name,
            userInfo.cpf,
            userInfo.address,
            userInfo.cep,
            (boletoPayment.value / 100.0).toString())

        val paymentResult = paymentClient.boletoPayment(paymentIn) ?: return PaymentResult.ERROR

        logger.debug { paymentResult }

        return PaymentResult.AUTHORIZED

    }

    fun payCreditCard(creditCardPayment: CreditCardPayment): PaymentResult {

        val creditCard = creditCardPayment.creditCard
        val userInfo = creditCardPayment.userInfo

        val paymentIn = PaymentInCreditCard(creditCard.holderName,
            userInfo.cpf,
            creditCard.cardNumber.toString(),
            creditCard.month.toString(),
            creditCard.year.toString(),
            creditCard.cvv.toString(),
            (creditCardPayment.value / 100.0).toString(),
            creditCardPayment.instalments.toString())

        paymentClient.creditCardPayment(paymentIn) ?: return PaymentResult.ERROR

        return PaymentResult.AUTHORIZED
    }

    fun getPaymentStatus(code: String): StatusBoleto {
        paymentClient.statusBoletoPayment(code)
        TODO("then what?")
    }

}