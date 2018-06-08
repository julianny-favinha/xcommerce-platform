package com.mc851.xcommerce.service.sac

import com.mc851.xcommerce.clients.SacClient
import com.mc851.xcommerce.clients.sac.api.MessageAPI
import com.mc851.xcommerce.clients.sac.api.TicketAPI
import com.mc851.xcommerce.clients.sac.api.TicketsAPI
import com.mc851.xcommerce.model.api.Message
import com.mc851.xcommerce.model.api.Ticket
import java.sql.Timestamp

class SacService(val sacClient: SacClient) {

    private val closed = 1
    private val deleted = 2

    private fun convertTicket(ticket: TicketAPI): Ticket {
        val message = mutableListOf<Message>()

        for(m in ticket.messages){
            message.add(Message(timestamp = m.timestamp, sender = m.sender, message = m.message))
        }

        return Ticket(
                ticketId = ticket.ticketId,
                userId = ticket.clienteId,
                compraId = ticket.compraId,
                statusId = ticket.statusId,
                messages = message
        )
    }

    private fun convertListTicket(tickets: TicketsAPI): List<Ticket> {
        val converted = mutableListOf<Ticket>()

        for(t in tickets.ticketList){
            // check if ticket has been deleted
            if(!t.statusId.equals(deleted)){
                converted.add(convertTicket(t))
            }
        }

        return converted
    }

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

    fun addTicket(userId: Long, message: Message) : List<Ticket>? {

        val messageApi = MessageAPI(
                timestamp = message.timestamp,
                sender = message.sender,
                message = message.message
        )

        val ticketId = sacClient.addTicket(userId, messageApi) ?: return null

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
}