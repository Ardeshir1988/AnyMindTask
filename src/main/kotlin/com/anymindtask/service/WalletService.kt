package com.anymindtask.service

import com.anymindtask.domain.Wallet
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface WalletService {
    fun increaseBalance(amount: BigDecimal): Mono<Wallet>
    fun initWallet()
    fun getBalance():Mono<BigDecimal>
}