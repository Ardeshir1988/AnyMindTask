package com.anymindtask.service

import com.anymindtask.controller.dto.TimePeriod
import com.anymindtask.domain.WalletTransaction
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface WalletTransactionService {
    fun saveTransaction(walletTransaction: WalletTransaction): Mono<WalletTransaction>
    fun findAllWalletTransactionBetween(period: TimePeriod): Flux<WalletTransaction>
}