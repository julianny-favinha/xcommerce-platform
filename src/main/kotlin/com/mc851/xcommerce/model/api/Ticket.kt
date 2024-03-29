package com.mc851.xcommerce.model.api

import java.sql.Timestamp

data class MessageOut(val timestamp: String,
                      val sender: String,
                      val message: String)

data class MessageIn(val sender: String,
                     val message: String)