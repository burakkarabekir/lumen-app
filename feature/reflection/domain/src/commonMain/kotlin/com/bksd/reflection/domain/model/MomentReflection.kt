package com.bksd.reflection.domain.model

import com.bksd.reflection.domain.support.SupportResource
import kotlinx.serialization.Serializable

@Serializable
sealed interface MomentReflection {
    val analysis: EntryAnalysis

    @Serializable
    data class Reflection(
        override val analysis: EntryAnalysis,
        val message: String,
        val question: String?
    ) : MomentReflection

    @Serializable
    data class Support(
        override val analysis: EntryAnalysis,
        val message: String,
        val mentalHealthLines: List<SupportResource>
    ) : MomentReflection

    @Serializable
    data class Crisis(
        override val analysis: EntryAnalysis,
        val message: String,
        val emergency: SupportResource,
        val crisisLines: List<SupportResource>
    ) : MomentReflection
}
