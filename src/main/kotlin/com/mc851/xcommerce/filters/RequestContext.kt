package com.mc851.xcommerce.filters

data class RequestContext(val userId: Long?) {
    constructor() : this(null)
    companion object {
        const val CONTEXT = "_CONTEXT"
    }
}