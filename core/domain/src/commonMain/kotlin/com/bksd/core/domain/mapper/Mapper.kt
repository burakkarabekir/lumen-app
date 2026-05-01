package com.bksd.core.domain.mapper

/**
 * Generic contract for mapping from input type [I] to output type [O].
 *
 * Declared as a `fun interface` so simple mappers can be expressed as lambdas.
 * For bidirectional mapping, compose two separate [Mapper] instances
 * (e.g., `Mapper<A, B>` and `Mapper<B, A>`) to respect Interface Segregation.
 */
fun interface Mapper<in I, out O> {
    fun map(input: I): O
}
