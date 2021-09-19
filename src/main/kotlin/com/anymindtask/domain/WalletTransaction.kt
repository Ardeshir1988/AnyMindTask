package com.anymindtask.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal
import java.util.*

data class WalletTransaction(
        @Id
        val id: String? = null,
        @Field(targetType = FieldType.DECIMAL128)
        val amount: BigDecimal? = null,
        @Field(targetType = FieldType.DECIMAL128)
        val balanceBefore: BigDecimal? = null,
        @Field(targetType = FieldType.DECIMAL128)
        val balanceAfter: BigDecimal? = null,
        var datetime: Date)