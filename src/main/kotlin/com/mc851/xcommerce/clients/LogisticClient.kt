package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.logistic.api.LogisticPriceInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticPriceOutAPI
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackOutApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterOutApi
import com.mc851.xcommerce.clients.product01.api.CategoryApi
import com.mc851.xcommerce.clients.product01.api.ProductApi
import java.util.UUID

interface LogisticClient {

    fun calculateShipment(logisticPriceIn: LogisticPriceInApi): LogisticPriceOutAPI?

    fun trackOrder(logisticTrackIn: LogisticTrackInApi): LogisticTrackOutApi?

    fun register(logisticRegisterIn: LogisticRegisterInApi): LogisticRegisterOutApi?

}