package com.mc851.xcommerce.clients

import com.mc851.xcommerce.clients.sac.api.CodeAPI
import com.mc851.xcommerce.clients.sac.api.MessageAPI
import com.mc851.xcommerce.clients.sac.api.TicketsAPI

interface SacClient {

    fun findTicketByUserId(userId: Long) : TicketsAPI?

    fun findTicketByCompraId(userId: Long, compraId: Long) : TicketsAPI?

    fun findTicketByTicketId(userId: Long, ticketId: Long) : TicketsAPI?

    fun addTicketToCompra(userId: Long, compraId: Long, message: MessageAPI) : CodeAPI?

    fun addTicket(userId: Long, message: MessageAPI) : CodeAPI?

    fun addMessageToTicket(userId: Long, ticketId: Long, message: MessageAPI) : TicketsAPI?

    fun changeTicketCode(userId: Long, ticketId: Long, code: Int, message: MessageAPI): CodeAPI?
}