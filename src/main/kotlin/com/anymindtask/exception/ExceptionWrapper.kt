package com.anymindtask.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.*

@Component
@Order(-1)
internal class ExceptionWrapper : WebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val bytes = ErrorResponse(message = ex.message!!, timeStamp = Date().toString()).toString().toByteArray(StandardCharsets.UTF_8)
        val buffer = exchange.response.bufferFactory().wrap(bytes)
        when (ex) {
            is BadRequestException -> {
                exchange.response.statusCode = HttpStatus.BAD_REQUEST
                exchange.response.headers.set("Access-Control-Allow-Origin", "*")
                exchange.response.headers.set("Content-Type", "application/json")
                return exchange.response.writeWith(Mono.just(buffer))
            }
            is MethodArgumentNotValidException -> {
                exchange.response.statusCode = HttpStatus.BAD_REQUEST
                exchange.response.headers.set("Access-Control-Allow-Origin", "*")
                exchange.response.headers.set("Content-Type", "application/json")
                return exchange.response.writeWith(Mono.just(buffer))
            }
            is MethodArgumentTypeMismatchException ->{
                exchange.response.statusCode = HttpStatus.NOT_ACCEPTABLE
                exchange.response.headers.set("Access-Control-Allow-Origin", "*")
                exchange.response.headers.set("Content-Type", "application/json")
                return exchange.response.writeWith(Mono.just(buffer))
            }
            is ServerWebInputException ->{
                exchange.response.statusCode = HttpStatus.NOT_ACCEPTABLE
                exchange.response.headers.set("Access-Control-Allow-Origin", "*")
                exchange.response.headers.set("Content-Type", "application/json")
                println(ex.mostSpecificCause.message)
                val fieldName = (ex.mostSpecificCause as MissingKotlinParameterException).parameter.name
                val specificByte = ErrorResponse(message = "$fieldName is null", timeStamp = Date().toString()).toString().toByteArray(StandardCharsets.UTF_8)
                val specificBuffer = exchange.response.bufferFactory().wrap(specificByte)
                return exchange.response.writeWith(Mono.just(specificBuffer))
            }
        }
        return Mono.error(ex)
    }
}