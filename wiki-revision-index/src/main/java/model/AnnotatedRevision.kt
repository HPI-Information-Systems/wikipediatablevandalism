package model

import java.math.BigInteger

data class AnnotatedRevision(
        val id: BigInteger,
        val revertId: BigInteger = 0.toBigInteger(),
        val hasComment: Boolean = false,
        val isVandalism: Boolean = false,
        val isReverted: Boolean = false,
        val isClueBot: Boolean = false
) {
    fun isCandidate(): Boolean {
        return isVandalism || isReverted || isClueBot
    }
}
