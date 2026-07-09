# Keep line numbers for readable crash reports; hide the original source file name.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*, InnerClasses, Signature, Exceptions, EnclosingMethod

# ---------------- kotlinx.serialization ----------------
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
# App @Serializable models, generated serializers, and Companions.
-keep,includedescriptorclasses class com.bksd.**$$serializer { *; }
-keepclassmembers class com.bksd.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}
-keep @kotlinx.serialization.Serializable class com.bksd.** { *; }
# Sealed nav routes are (de)serialized polymorphically via NavKey.
-keep class com.bksd.lumen.navigation.route.** { *; }

# ---------------- Enums ----------------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ---------------- Coroutines ----------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }
-dontwarn kotlinx.coroutines.**

# ---------------- Ktor (Supabase transport) ----------------
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }
-dontwarn io.ktor.**
-dontwarn org.slf4j.**

# ---------------- Supabase (supabase-kt) ----------------
-keep class io.github.jan.supabase.** { *; }
-dontwarn io.github.jan.supabase.**

# ---------------- RevenueCat ----------------
-keep class com.revenuecat.** { *; }
-dontwarn com.revenuecat.**

# ---------------- Room ----------------
-keep class * extends androidx.room.RoomDatabase { <init>(); }
-dontwarn androidx.room.paging.**

# ---------------- Misc reflective libraries ----------------
-dontwarn org.conscrypt.**
-dontwarn javax.annotation.**
