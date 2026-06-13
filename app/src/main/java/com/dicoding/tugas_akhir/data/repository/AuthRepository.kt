package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.domain.model.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepository private constructor(
    private val firebaseAuth: FirebaseAuth,
) {

    fun observeAuthState(): Flow<UserSession?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toUserSession())
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    fun getCurrentUser(): UserSession? {
        return firebaseAuth.currentUser?.toUserSession()
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toUserSession(): UserSession {
        return UserSession(
            uid = uid,
            name = displayName,
            email = email,
            photoUrl = photoUrl?.toString(),
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance(
            firebaseAuth: FirebaseAuth,
        ): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthRepository(firebaseAuth)
                INSTANCE = instance
                instance
            }
        }
    }
}