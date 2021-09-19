package com.anymindtask.service

import com.anymindtask.domain.Wallet
import com.anymindtask.repository.WalletRepository
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.math.BigDecimal
import javax.annotation.PostConstruct

@Service
class WalletServiceImpl(private val walletRepository: WalletRepository,
                        private val reactiveMongoTemplate: ReactiveMongoTemplate) : WalletService {

    val staticId = "anyMindWalletId"

    override fun increaseBalance(amount: BigDecimal) =
            reactiveMongoTemplate.findAndModify(Query.query(Criteria.where("id").`is`(staticId)),
                    Update().inc("balance", amount),
                    FindAndModifyOptions.options().returnNew(true),
                    Wallet().javaClass)

    override fun getBalance(): Mono<BigDecimal> =
            walletRepository.findById(staticId).map { wallet -> wallet.balance!! }


    @PostConstruct
    override fun initWallet() {
        walletRepository.findById(staticId)
                .switchIfEmpty { walletRepository.save(Wallet(id = staticId, balance = 1000.0.toBigDecimal())) }.subscribe()
    }
}