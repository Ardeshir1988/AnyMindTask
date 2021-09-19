package com.anymindtask.repository

import com.anymindtask.domain.WalletTransaction
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface WalletTransactionRepository : ReactiveMongoRepository<WalletTransaction, String> {
    fun findAllByDatetimeBetween(startDate: Date, endDate: Date): Flux<WalletTransaction>
}