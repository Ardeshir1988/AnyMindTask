package com.anymindtask.controller.dto

import java.time.OffsetDateTime

data class TimePeriod(val startDatetime: OffsetDateTime,
                      val endDatetime: OffsetDateTime)