package com.bksd.core.data.remote.firebase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

class FirebaseFirestoreDataSource {
    private val firestore by lazy {
        Firebase.firestore.apply {
            setLoggingEnabled(true)
        }
    }

    suspend fun <T : Any> getDocument(
        collectionPath: String,
        documentId: String,
        deserializer: DeserializationStrategy<T>
    ): Result<T?, AppError> {
        println("[Firestore] getDocument: $collectionPath/$documentId")
        return try {
            val snapshot = firestore
                .collection(collectionPath)
                .document(documentId)
                .get()
            if (!snapshot.exists) {
                println("[Firestore] getDocument: $collectionPath/$documentId -> not found")
                Result.Success(null)
            } else {
                val data = snapshot.data(deserializer)
                println("[Firestore] getDocument: $collectionPath/$documentId -> success")
                Result.Success(data)
            }
        } catch (e: Exception) {
            println("[Firestore] getDocument: $collectionPath/$documentId -> error=${e.message}")
            Result.Error(mapFirestoreError(e))
        }
    }

    suspend fun <T : Any> getDocuments(
        collectionPath: String,
        deserializer: DeserializationStrategy<T>
    ): Result<List<T>, AppError> {
        println("[Firestore] getDocuments: $collectionPath")
        return try {
            val snapshot = firestore
                .collection(collectionPath)
                .get()
            val items = snapshot.documents.map { it.data(deserializer) }
            println("[Firestore] getDocuments: $collectionPath -> ${items.size} items")
            Result.Success(items)
        } catch (e: Exception) {
            println("[Firestore] getDocuments: $collectionPath -> error=${e.message}")
            Result.Error(mapFirestoreError(e))
        }
    }

    suspend fun <T : Any> queryDocuments(
        collectionPath: String,
        field: String,
        greaterThanOrEqual: Any,
        lessThan: Any,
        deserializer: DeserializationStrategy<T>
    ): Result<List<T>, AppError> {
        println("[Firestore] queryDocuments: $collectionPath, field=$field, gte=$greaterThanOrEqual, lt=$lessThan")
        return try {
            val snapshot = firestore
                .collection(collectionPath)
                .where { field.greaterThanOrEqualTo(greaterThanOrEqual) }
                .where { field.lessThan(lessThan) }
                .get()
            val items = snapshot.documents.map { it.data(deserializer) }
            println("[Firestore] queryDocuments: $collectionPath -> ${items.size} items")
            Result.Success(items)
        } catch (e: Exception) {
            println("[Firestore] queryDocuments: $collectionPath -> error=${e.message}")
            Result.Error(mapFirestoreError(e))
        }
    }

    suspend fun <T : Any> setDocument(
        collectionPath: String,
        documentId: String,
        data: T,
        serializer: SerializationStrategy<T>
    ): Result<Unit, AppError> {
        println("[Firestore] setDocument: $collectionPath/$documentId")
        return try {
            firestore
                .collection(collectionPath)
                .document(documentId)
                .set(serializer, data)
            println("[Firestore] setDocument: $collectionPath/$documentId -> success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[Firestore] setDocument: $collectionPath/$documentId -> error=${e.message}")
            Result.Error(mapFirestoreError(e))
        }
    }

    suspend fun deleteDocument(
        collectionPath: String,
        documentId: String
    ): Result<Unit, AppError> {
        println("[Firestore] deleteDocument: $collectionPath/$documentId")
        return try {
            firestore
                .collection(collectionPath)
                .document(documentId)
                .delete()
            println("[Firestore] deleteDocument: $collectionPath/$documentId -> success")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("[Firestore] deleteDocument: $collectionPath/$documentId -> error=${e.message}")
            Result.Error(mapFirestoreError(e))
        }
    }

    private fun mapFirestoreError(e: Exception): AppError {
        val msg = e.message?.lowercase() ?: ""
        return when {
            msg.contains("permission") || msg.contains("denied") ->
                AppError.Network(NetworkErrorType.UNAUTHORIZED)
            msg.contains("not found") ->
                AppError.Network(NetworkErrorType.SERVER_ERROR)
            msg.contains("unavailable") || msg.contains("network") ->
                AppError.Network(NetworkErrorType.NO_INTERNET)
            msg.contains("deadline") || msg.contains("timeout") ->
                AppError.Network(NetworkErrorType.REQUEST_TIMEOUT)
            else -> AppError.Unknown(e.message ?: "Firestore error")
        }
    }
}
