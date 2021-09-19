package com.anymindtask.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal

data class Wallet(
        @Id
        val id: String? = null,
        @Field(targetType = FieldType.DECIMAL128)
        val balance: BigDecimal? = null)