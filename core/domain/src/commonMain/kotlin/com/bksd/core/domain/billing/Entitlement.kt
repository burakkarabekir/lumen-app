package com.bksd.core.domain.billing

data class Entitlement(
    val isPlus: Boolean,
) {
    companion object {
        val Free = Entitlement(isPlus = false)
    }
}
