package com.mc851.xcommerce.clients.sac.api

import java.sql.Timestamp

data class TicketsAPI(val ticketSize: Int, val ticketList: List<TicketAPI>)

data class TicketAPI(val ticketId: Long,
                  val clienteId: Long,
                  val compraId: Long,
                  val statusId: Long,
                  val messages: List<MessageAPI>)

data class MessageAPI(val timestamp: Timestamp, val sender: String, val message: String)

data class CodeAPI(val systemMessage: String)