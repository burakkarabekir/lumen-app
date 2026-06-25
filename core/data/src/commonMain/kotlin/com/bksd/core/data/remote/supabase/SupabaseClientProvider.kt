package com.bksd.core.data.remote.supabase

import com.bksd.core.data.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.serialization.json.Json

fun createLumenSupabaseClient(): SupabaseClient = createSupabaseClient(
    supabaseUrl = BuildKonfig.SUPABASE_URL,
    supabaseKey = BuildKonfig.SUPABASE_ANON_KEY,
) {
    defaultSerializer = KotlinXSerializer(
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    )
    install(Auth)
    install(Postgrest)
    install(Storage)
}
