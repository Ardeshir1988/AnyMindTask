package com.anymindtask.service

import com.anymindtask.controller.dto.ApiResponse
import com.anymindtask.controller.dto.TimePeriod
import com.anymindtask.controller.dto.WalletBalanceHistoryDto
import com.anymindtask.controller.dto.WalletTransactionDto
import com.anymindtask.domain.WalletTransaction
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
class HighLevelServiceImpl(private val walletService: WalletService,
                           private val walletTransactionService: WalletTransactionService) : HighLevelService {

    override fun handleNewTransaction(walletTransactionDto: WalletTransactionDto) =
            walletService.getBalance()
                    .flatMap { balance ->
                        walletTransactionService.saveTransaction(walletTransactionDto(walletTransactionDto = walletTransactionDto, balance = balance))
                    }
                    .flatMap { walletTransaction ->
                        walletService.increaseBalance(walletTransaction.amount!!)
                    }
                    .map { ApiResponse("transaction saved") }


    private fun walletTransactionDto(walletTransactionDto: WalletTransactionDto, balance: BigDecimal) =
            WalletTransaction(amount = walletTransactionDto.amount, datetime = walletTransactionDto.datetime,
                    balanceBefore = balance, balanceAfter = balance.plus(walletTransactionDto.amount))


    override fun getWalletHistoryBetween(period: TimePeriod): Flux<WalletBalanceHistoryDto> {
        return walletTransactionService.findAllWalletTransactionBetween(period)
                .collectList()
                .flatMapIterable { transactions -> transactionListToBalanceHistoryDtoList(transactions, period) }
    }

    private fun transactionListToBalanceHistoryDtoList(transactions: List<WalletTransaction>, period: TimePeriod): List<WalletBalanceHistoryDto> {
        return createHourSlots(period)
                .map { oneHourSlot ->
                    val lastTransaction = transactions
                            .filter { transaction ->
                                transaction.datetime.after(offsetDateTimeToDate(oneHourSlot.first)) &&
                                        transaction.datetime.before(offsetDateTimeToDate(oneHourSlot.second))
                            }
                            .maxByOrNull { transaction -> transaction.balanceAfter!! }

                    if (lastTransaction !== null) {
                        return@map Pair(lastTransaction.balanceAfter!!, oneHourSlot.second);
                    } else {
                        return@map Pair(BigDecimal.ZERO, oneHourSlot.second)
                    }
                }
                .filter { p -> p.first > BigDecimal.ZERO }
                .map { transaction -> walletTransactionToBalanceHistoryDto(transaction) }
    }

    private fun createHourSlots(period: TimePeriod): MutableList<Pair<OffsetDateTime, OffsetDateTime>> {
        var startDate = OffsetDateTime.of(period.startDatetime.year,
                period.startDatetime.monthValue,
                period.startDatetime.dayOfMonth,
                period.startDatetime.hour,
                0,
                0,
                0,
                period.startDatetime.offset)
        val endDate = OffsetDateTime.of(period.endDatetime.year,
                period.endDatetime.monthValue,
                period.endDatetime.dayOfMonth,
                period.endDatetime.hour,
                0,
                0,
                0,
                period.endDatetime.offset)
        val timeSlots: MutableList<Pair<OffsetDateTime, OffsetDateTime>> = mutableListOf()
        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusHours(1)
            if (startDate.plusHours(1).isBefore(endDate.plusNanos(1)))
                timeSlots.add(Pair(startDate, startDate.plusHours(1)))
        }
        return timeSlots
    }

    private fun walletTransactionToBalanceHistoryDto(balancePeriod: Pair<BigDecimal, OffsetDateTime>): WalletBalanceHistoryDto {
        return WalletBalanceHistoryDto(balancePeriod.first, offsetDateTimeToDate(balancePeriod.second))
    }
}