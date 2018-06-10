package com.mc851.xcommerce.service.sac

import com.mc851.xcommerce.clients.SacClient
import com.mc851.xcommerce.clients.sac.api.MessageAPI
import com.mc851.xcommerce.clients.sac.api.TicketsAPI
import com.mc851.xcommerce.model.api.MessageIn
import com.mc851.xcommerce.model.api.MessageOut
import mu.KotlinLogging
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

class SacService(val sacClient: SacClient) {

    private val log = KotlinLogging.logger {}

    private fun convertListMessages(tickets: TicketsAPI): List<MessageOut> {
        val messages = mutableListOf<MessageOut>()

        val ticket = tickets.ticketList!![0]

        for(m in ticket.messagesList){
            messages.add(MessageOut(timestamp = m.timestamp, sender = m.sender, message = m.message))
        }

        return messages.sortedWith(compareByDescending { it.timestamp })
    }

    fun getMessages(userId: Long): List<MessageOut>? {
        val tickets = sacClient.findTicketByUserId(userId) ?: return emptyList()

        if(tickets.ticketList == null || tickets.ticketSize == 0) {
            return emptyList()
        }
        return convertListMessages(tickets)
    }

    fun sendMessage(userId: Long, message: MessageIn): Boolean {
        val timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()).dropLast(3)

        val messageApi = MessageAPI(
                timestamp = timestamp,
                sender = message.sender,
                message = message.message
        )

        val tickets = sacClient.findTicketByUserId(userId)

        log.info { tickets }
        log.info { messageApi }

        if(tickets == null){
            val ticket = sacClient.addTicket(userId, messageApi) ?: return false
            sacClient.addMessageToTicket(userId, ticket.systemMessage.toLong(), messageApi)
        } else {
            val ticketId = tickets.ticketList!![0].ticketId
            sacClient.addMessageToTicket(userId, ticketId.toLong(), messageApi) ?: return false
        }

        return true
    }

    /*
    fun getByUserId(userId: Long) : List<Ticket>? {
        val tickets = sacClient.findTicketByUserId(userId) ?: return null

        return convertListTicket(tickets)
    }

    fun getByCompraId(userId: Long, compraId: Long) : List<Ticket>? {
        val tickets = sacClient.findTicketByCompraId(userId, compraId) ?: return null

        return convertListTicket(tickets)
    }

    fun getByTicketId(userId: Long, ticketId: Long) : List<Ticket>? {
        val tickets = sacClient.findTicketByTicketId(userId, ticketId) ?: return null

        return convertListTicket(tickets)
    }

    fun addTicketCompra(userId: Long, compraId: Long, message: Message) : List<Ticket>? {

        val messageApi = MessageAPI(
                timestamp = message.timestamp,
                sender = message.sender,
                message = message.message
        )

        val ticketId = sacClient.addTicketToCompra(userId, compraId, messageApi) ?: return null

        val tickets = sacClient.findTicketByTicketId(userId, ticketId.systemMessage.toLong()) ?: return null

        return convertListTicket(tickets)
    }

    fun addMessage(userId: Long, ticketId: Long, message: Message) : List<Ticket>? {

        val messageApi = MessageAPI(
                timestamp = message.timestamp,
                sender = message.sender,
                message = message.message
        )

        sacClient.addMessageToTicket(userId, ticketId, messageApi) ?: return null

        val tickets = sacClient.findTicketByTicketId(userId, ticketId) ?: return null

        return convertListTicket(tickets)
    }

    fun closeTicket(userId: Long, ticketId: Long) : List<Ticket>? {
        sacClient.findTicketByTicketId(userId, ticketId) ?: throw IllegalStateException("Ticket not found!")

        // A message is needed
        val timestamp = Timestamp(System.currentTimeMillis())
        val message = MessageAPI(timestamp = timestamp, sender = "system", message = "closed")

        sacClient.changeTicketCode(userId, ticketId, closed, message) ?: return null

        val codeChanged = sacClient.findTicketByTicketId(userId, ticketId) ?: throw IllegalStateException("Ticket not found!")

        // return tickets after code has changed
        return convertListTicket(codeChanged)
    }

    // Note que a mudança de estado para 'deleted' *não* deleta o ticket do servidor
    fun deleteTicket(userId: Long, ticketId: Long) : List<Ticket>? {
        val tickets = sacClient.findTicketByTicketId(userId, ticketId) ?: throw IllegalStateException("Ticket not found!")

        // A message is needed
        val timestamp = Timestamp(System.currentTimeMillis())
        val message = MessageAPI(timestamp = timestamp, sender = "system", message = "deleted")

        sacClient.changeTicketCode(userId, ticketId, deleted, message) ?: return null

        // return tickets before its deletion
        return convertListTicket(tickets)
    }
    */
}