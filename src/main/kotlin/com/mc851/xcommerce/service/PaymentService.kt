package com.mc851.xcommerce.service

import com.mc851.xcommerce.clients.payment.PaymentClient
import com.mc851.xcommerce.clients.payment.api.PaymentInBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentInCreditCard
import com.mc851.xcommerce.model.BoletoPayment
import com.mc851.xcommerce.model.CreditCardPayment
import com.mc851.xcommerce.model.PaymentResult

class PaymentService(private val paymentClient: PaymentClient) {

    fun payBoleto(boletoPayment: BoletoPayment): PaymentResult {

        val userInfo = boletoPayment.userInfo

        val paymentIn = PaymentInBoleto(userInfo.name,
            userInfo.cpf,
            userInfo.address,
            userInfo.cep,
            (boletoPayment.value / 100.0).toString())

        val paymentResult = paymentClient.boletoPayment(paymentIn) ?: PaymentResult.ERROR

        System.out.println(paymentResult.toString())

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
            creditCardPayment.installments.toString())

        val paymentResult = paymentClient.creditCardPayment(paymentIn) ?: return PaymentResult.ERROR

        System.out.println(paymentResult.toString())

        return PaymentResult.AUTHORIZED
    }

    fun getPaymentStatus() {
        TODO()
    }

}