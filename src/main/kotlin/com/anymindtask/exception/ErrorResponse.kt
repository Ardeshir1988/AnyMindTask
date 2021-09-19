package com.anymindtask.exception

data class ErrorResponse(val message: String, val timeStamp: String) {
    override fun toString(): String {
        return """{"message":"$message","timeStamp":"$timeStamp"}""".trimIndent()
    }
}