package com.mc851.xcommerce.model.internal

data class CreditCardPayment(val creditCard: CreditCardInfo,
                             val userInfo: UserInfo,
                             val value: Long,
                             val instalments: Long)

data class BoletoPayment(val userInfo: UserInfo, val value: Long)

data class CreditCardInfo(val holderName: String, val month: Long, val year: Long, val cvv: Long, val cardNumber: Long)

data class UserInfo(val cpf: String, val name: String, val address: String, val cep: String)

enum class PaymentResultStatus(val paymentStatus: PaymentStatus) {
    AUTHORIZED(PaymentStatus.OK),
    FAILED(PaymentStatus.ERROR),
    PENDING(PaymentStatus.PENDING),
    ERROR(PaymentStatus.ERROR);
}

data class PaymentResult(val status: PaymentResultStatus, val code: String?) {
    constructor(status: PaymentResultStatus) : this(status, null)
}
