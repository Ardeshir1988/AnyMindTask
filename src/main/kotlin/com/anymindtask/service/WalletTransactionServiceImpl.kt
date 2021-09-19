package com.anymindtask.service

import com.anymindtask.controller.dto.TimePeriod
import com.anymindtask.domain.WalletTransaction
import com.anymindtask.exception.BadRequestException
import com.anymindtask.repository.WalletTransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.util.*

@Service
class WalletTransactionServiceImpl(val walletTransactionRepository: WalletTransactionRepository)
    : WalletTransactionService {
    override fun saveTransaction(walletTransaction: WalletTransaction): Mono<WalletTransaction> {
        if (walletTransaction.datetime.before(Date.from(OffsetDateTime.now().minusDays(10).toInstant())))
            throw BadRequestException("datetime not be older than 10 days ago")
        return walletTransactionRepository.save(walletTransaction)
    }

    override fun findAllWalletTransactionBetween(period: TimePeriod): Flux<WalletTransaction> {
        return walletTransactionRepository.findAllByDatetimeBetween(Date.from(period.startDatetime.toInstant()),
                Date.from(period.endDatetime.toInstant()))
    }

}