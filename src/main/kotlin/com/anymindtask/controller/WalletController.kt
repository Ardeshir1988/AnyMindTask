package com.anymindtask.controller

import com.anymindtask.controller.dto.*
import com.anymindtask.exception.BadRequestException
import com.anymindtask.service.HighLevelService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.Error
import javax.validation.Valid

@RestController
@RequestMapping("/wallet")
@Validated
class WalletController(val highLevelService: HighLevelService) {
    @PostMapping
    fun addNewTransaction(@RequestBody @Valid walletTransactionDto: WalletTransactionDto): Mono<ApiResponse> {
        return highLevelService.handleNewTransaction(walletTransactionDto)
                .doOnError { e -> Error("test") }
    }

    @PostMapping("/history")
    fun getTransaction(@RequestBody period: TimePeriod): Flux<WalletBalanceHistoryDto> {
        return highLevelService.getWalletHistoryBetween(period)
    }
}