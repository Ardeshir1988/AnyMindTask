package com.anymindtask.service

import com.anymindtask.controller.dto.ApiResponse
import com.anymindtask.controller.dto.TimePeriod
import com.anymindtask.controller.dto.WalletBalanceHistoryDto
import com.anymindtask.controller.dto.WalletTransactionDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.*

interface HighLevelService {
    fun handleNewTransaction(walletTransactionDto: WalletTransactionDto): Mono<ApiResponse>
    fun getWalletHistoryBetween(period: TimePeriod): Flux<WalletBalanceHistoryDto>
    fun offsetDateTimeToDate(offsetDateTime: OffsetDateTime): Date {
        return Date.from(offsetDateTime.toInstant())
    }
}