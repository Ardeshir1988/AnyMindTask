package com.anymindtask.repository

import com.anymindtask.domain.Wallet
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : ReactiveMongoRepository<Wallet, String> {
}