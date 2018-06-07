package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.logistic.api.LogisticInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticOutAPI
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticRegisterOutApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackInApi
import com.mc851.xcommerce.clients.logistic.api.LogisticTrackOutApi

interface LogisticClient {

    fun calculateShipment(logisticPriceIn: LogisticInApi): LogisticOutAPI?

    fun trackOrder(logisticTrackIn: LogisticTrackInApi): LogisticTrackOutApi?

    fun register(logisticRegisterIn: LogisticRegisterInApi): LogisticRegisterOutApi?

}