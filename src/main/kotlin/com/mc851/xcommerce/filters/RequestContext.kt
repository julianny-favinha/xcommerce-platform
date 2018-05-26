package com.mc851.xcommerce.filters

data class RequestContext(val userId: Long) {
    companion object {
        const val CONTEXT = "_CONTEXT"
    }
}