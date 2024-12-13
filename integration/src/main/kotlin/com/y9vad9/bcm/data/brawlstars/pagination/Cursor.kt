package com.y9vad9.bcm.data.brawlstars.pagination

import com.y9vad9.bcm.foundation.validation.ValueConstructor
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Cursor private constructor(val value: String) {
    companion object : ValueConstructor<Cursor, String> {
        override val displayName: String = "Cursor"

        override fun create(value: String): Result<Cursor> {
            return Result.success(Cursor(value))
        }
    }
}