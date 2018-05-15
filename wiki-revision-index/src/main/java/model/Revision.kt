package model

import java.math.BigInteger
import java.time.Instant
import java.time.LocalDateTime

data class Revision(
        val id: BigInteger,
        val pageId: BigInteger,
        val createdAt: Instant,
        val tableCount: Int,
        val changedTables: Int
)
