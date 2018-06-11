package com.mc851.xcommerce.service.payment

import com.mc851.xcommerce.clients.PaymentClient
import com.mc851.xcommerce.clients.payment.api.PaymentInBoleto
import com.mc851.xcommerce.clients.payment.api.PaymentInCreditCard
import com.mc851.xcommerce.model.internal.BoletoPayment
import com.mc851.xcommerce.model.internal.CreditCardPayment
import com.mc851.xcommerce.model.internal.PaymentResult
import com.mc851.xcommerce.model.internal.PaymentResultStatus
import com.mc851.xcommerce.model.internal.PaymentStatus
import com.mc851.xcommerce.service.user.CreditService

class PaymentService(private val paymentClient: PaymentClient,
                     private val creditService: CreditService) {

    fun payBoleto(boletoPayment: BoletoPayment): PaymentResult {

        val userInfo = boletoPayment.userInfo

        if (!creditService.validateUser(userInfo.cpf, boletoPayment.value)) { return PaymentResult(PaymentResultStatus.FAILED) }

        val paymentIn = PaymentInBoleto(userInfo.name,
            userInfo.cpf,
            userInfo.address,
            userInfo.cep,
            (boletoPayment.value / 100.0).toString())

        val paymentResponse = paymentClient.boletoPayment(paymentIn) ?: return PaymentResult(PaymentResultStatus.ERROR)

        return when (paymentResponse.result) {
            "AUTHORIZED" -> PaymentResult(PaymentResultStatus.PENDING, paymentResponse.code)
            "UNAUTHORIZED" -> PaymentResult(PaymentResultStatus.FAILED)
            else -> PaymentResult(PaymentResultStatus.ERROR)
        }
    }

    fun payCreditCard(creditCardPayment: CreditCardPayment): PaymentResult {

        val creditCard = creditCardPayment.creditCard
        val userInfo = creditCardPayment.userInfo

        if (!creditService.validateUser(userInfo.cpf, creditCardPayment.value)) { return PaymentResult(PaymentResultStatus.FAILED) }

        val paymentIn = PaymentInCreditCard(creditCard.holderName,
            userInfo.cpf,
            creditCard.cardNumber.toString(),
            creditCard.month.toString(),
            creditCard.year.toString(),
            creditCard.cvv.toString(),
            (creditCardPayment.value / 100.0).toString(),
            creditCardPayment.instalments.toString())

        val paymentResponse =
            paymentClient.creditCardPayment(paymentIn) ?: return PaymentResult(PaymentResultStatus.ERROR)

        return when (paymentResponse.result) {
            "AUTHORIZED" -> PaymentResult(PaymentResultStatus.AUTHORIZED)
            "UNAUTHORIZED" -> PaymentResult(PaymentResultStatus.FAILED)
            else -> PaymentResult(PaymentResultStatus.ERROR)
        }
    }

    fun getPaymentStatus(code: String): PaymentStatus? {
        val statusBoletoPayment = paymentClient.statusBoletoPayment(code) ?: return null

        return when (statusBoletoPayment.status) {
            "PENDING_PAYMENT" -> PaymentStatus.PENDING
            "OK" -> PaymentStatus.OK
            "EXPIRED" -> PaymentStatus.EXPIRED
            else -> PaymentStatus.ERROR
        }
    }

}