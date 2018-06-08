package com.mc851.xcommerce.model.api

import java.sql.Timestamp

data class Ticket(val ticketId: Long,
                  val userId: Long,
                  val compraId: Long,
                  val statusId: Long,
                  val messages: List<Message>)

data class Message(val timestamp: Timestamp,
                   val sender: String,
                   val message: String)