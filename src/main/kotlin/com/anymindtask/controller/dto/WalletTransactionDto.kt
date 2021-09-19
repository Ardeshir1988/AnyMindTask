package com.anymindtask.controller.dto


import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.NotEmpty

class WalletTransactionDto(
        @field:NotEmpty(message = "not emptyyyyyyyyyyyy")
        val amount: BigDecimal,
        val datetime: Date
)

data class WalletBalanceHistoryDto(
        val amount: BigDecimal,
        val datetime: Date)