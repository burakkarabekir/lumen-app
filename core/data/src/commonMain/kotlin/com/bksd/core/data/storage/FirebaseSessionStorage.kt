package com.bksd.core.data.storage

import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.flow.Flow

class FirebaseSessionStorage(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : SessionStorage {

    override fun observeAuthState(): Flow<Boolean> =
        firebaseAuthDataSource.authState

    override fun isLoggedIn(): Boolean =
        firebaseAuthDataSource.getSignedInUserId() != null
}
