package com.mc851.xcommerce.clients.sac.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TicketsAPI(val ticketsList: List<TicketAPI>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TicketAPI(val ticketId: String,
                  val clienteId: String,
                  val messagesList: List<MessageAPI>)

data class MessageAPI(val timestamp: String, val sender: String, val message: String)

data class CodeAPI(val systemMessage: String)