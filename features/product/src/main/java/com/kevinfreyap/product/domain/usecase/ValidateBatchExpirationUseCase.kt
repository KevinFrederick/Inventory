package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.BatchExpirationError
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class ValidateBatchExpirationUseCase @Inject constructor() {
    operator fun invoke(rawDateMillis: Long?): Result<Long?, BatchExpirationError> {
        if (rawDateMillis == null) return Result.Success(null)

        val selectedDate = Instant.ofEpochMilli(rawDateMillis)
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()

        val today = LocalDate.now()

        return when {
            !selectedDate.isAfter(today) -> Result.Error(BatchExpirationError.CANNOT_BE_IN_PAST)
            else -> Result.Success(rawDateMillis)
        }
    }
}