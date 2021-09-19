package com.anymindtask.service

import com.anymindtask.controller.dto.TimePeriod
import com.anymindtask.domain.WalletTransaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Flux
import java.time.OffsetDateTime
import java.util.*

@SpringBootTest
class HighLevelServiceTest {
    @MockBean
    lateinit var walletTransactionService: WalletTransactionService

    @Autowired
    lateinit var highLevelService: HighLevelService

    @Test
    fun findAllWalletTransactionBetweenTest() {
        val requestPeriod = TimePeriod(startDatetime = OffsetDateTime.parse("2021-09-17T00:22:53.685+04:30"),
                endDatetime = OffsetDateTime.parse("2021-09-19T23:22:53.685+04:30"))
        val transactions = Flux.just(
                WalletTransaction(id = "6146f96683b64541a0fcc361", amount = 5.1.toBigDecimal(), balanceBefore = 1000.0.toBigDecimal(), balanceAfter = 1005.1.toBigDecimal(), datetime = Date(1631861573685)),
                WalletTransaction(id = "6146f99b83b64541a0fcc362", amount = 101.15.toBigDecimal(), balanceBefore = 1005.1.toBigDecimal(), balanceAfter = 1106.25.toBigDecimal(), datetime = Date(1632045173685)),
                WalletTransaction(id = "6146f9ae83b64541a0fcc363", amount = 1.5.toBigDecimal(), balanceBefore = 1106.25.toBigDecimal(), balanceAfter = 1107.75.toBigDecimal(), datetime = Date(1632055973685)),
                WalletTransaction(id = "6146f9b683b64541a0fcc364", amount = 1.5.toBigDecimal(), balanceBefore = 1107.75.toBigDecimal(), balanceAfter = 1109.25.toBigDecimal(), datetime = Date(1632063173685)),
                WalletTransaction(id = "6146f9b683b64541a0fcc364", amount = 2.2.toBigDecimal(), balanceBefore = 1109.25.toBigDecimal(), balanceAfter = 1111.45.toBigDecimal(), datetime = Date(1632063173685)))
        Mockito.`when`(walletTransactionService.findAllWalletTransactionBetween(period = requestPeriod)).thenReturn(transactions)

        val historyDtoList = highLevelService.getWalletHistoryBetween(period = requestPeriod).collectList().block()

        //we expect 4 time slots with one hour period with 5 transactions which are mocked
        Assertions.assertEquals(4, historyDtoList?.size)
    }
}