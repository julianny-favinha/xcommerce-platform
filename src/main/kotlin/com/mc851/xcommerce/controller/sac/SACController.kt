package com.mc851.xcommerce.controller.sac

import com.mc851.xcommerce.controller.utils.handleResponse
import com.mc851.xcommerce.filters.RequestContext
import com.mc851.xcommerce.model.api.MessageIn
import com.mc851.xcommerce.model.api.MessageOut
import com.mc851.xcommerce.service.sac.SacService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("sac")
class SACController {

    @Autowired
    lateinit var sacService: SacService

    @GetMapping("")
    fun retrieveAll(@ModelAttribute(RequestContext.CONTEXT) context: RequestContext) : ResponseEntity<List<MessageOut>> {
        val userId = context.userId!!
        val response = sacService.getMessages(userId)
        return handleResponse(response)
    }

    @PostMapping("/send")
    fun sendMessage(@ModelAttribute(RequestContext.CONTEXT) context: RequestContext, @RequestBody message: MessageIn) : ResponseEntity<Boolean>{
        val userId = context.userId!!
        val response = sacService.sendMessage(userId, message)
        return handleResponse(response)
    }

    /*
    // ok
    @GetMapping("/{userId}")
    fun getByUserId(@PathVariable(name = "userId", required = true) id: Long): ResponseEntity<List<Ticket>>{
        val response = sacService.getByUserId(id)
        return handleResponse(response)
    }

    // ok
    @GetMapping("/{userId}/compra/{compraId}")
    fun getByCompraId(@PathVariable(name = "userId", required = true) userId: Long,
                      @PathVariable(name = "compraId", required = true) compraId: Long): ResponseEntity<List<Ticket>>{
        val response = sacService.getByCompraId(userId, compraId)
        return handleResponse(response)
    }

    //ok
    @GetMapping("/{userId}/ticket/{ticketId}")
    fun getByTicketId(@PathVariable(name = "userId", required = true) userId: Long,
                      @PathVariable(name = "ticketId", required = true) ticketId: Long): ResponseEntity<List<Ticket>>{
        val response = sacService.getByTicketId(userId, ticketId)
        return handleResponse(response)
    }

    // ok
    @PostMapping("/{userId}/ticket/add")
    fun addTicket(@PathVariable(name = "userId", required = true) userId: Long,
                  @RequestBody message: Message) : ResponseEntity<List<Ticket>>{
        val response = sacService.addTicket(userId, message)
        return handleResponse(response)
    }

    // ok
    @PostMapping("/{userId}/compra/{compraId}/add")
    fun addTicketCompra(@PathVariable(name = "userId", required = true) userId: Long,
                   @PathVariable(name = "compraId", required = true) compraId: Long,
                   @RequestBody message: Message) : ResponseEntity<List<Ticket>>{
        val response = sacService.addTicketCompra(userId, compraId, message)
        return handleResponse(response)
    }

    // ok
    @PutMapping("/{userId}/ticket/{ticketId}/add")
    fun addMessageToTicket(@PathVariable(name = "userId", required = true) userId: Long,
                   @PathVariable(name = "ticketId", required = true) ticketId: Long,
                   @RequestBody message: Message) : ResponseEntity<List<Ticket>>{
        val response = sacService.addMessage(userId, ticketId, message)
        return handleResponse(response)
    }

    @DeleteMapping("/{userId}/ticket/{ticketId}/close")
    fun closeTicket(@PathVariable(name = "userId", required = true) userId: Long,
                   @PathVariable(name = "ticketId", required = true) ticketId: Long): ResponseEntity<List<Ticket>>{
        val response = sacService.closeTicket(userId, ticketId)
        return handleResponse(response)
    }

    @DeleteMapping("/{userId}/ticket/{ticketId}/close")
    fun deleteTicket(@PathVariable(name = "userId", required = true) userId: Long,
                    @PathVariable(name = "ticketId", required = true) ticketId: Long): ResponseEntity<List<Ticket>>{
        val response = sacService.deleteTicket(userId, ticketId)
        return handleResponse(response)
    }
    */
}