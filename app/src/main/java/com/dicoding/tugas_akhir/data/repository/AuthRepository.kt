package com.dicoding.tugas_akhir.data.repository

import com.dicoding.tugas_akhir.domain.model.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
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

    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.toAuthErrorMessage())
            }
    }

    fun registerWithEmail(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user

                if (user == null) {
                    onError("Registrasi gagal. Silakan coba lagi.")
                    return@addOnSuccessListener
                }

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                user.updateProfile(profileUpdates)
                    .addOnSuccessListener {
                        firebaseAuth.signOut()
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        onError(exception.toAuthErrorMessage())
                    }
            }
            .addOnFailureListener { exception ->
                onError(exception.toAuthErrorMessage())
            }
    }

    fun loginWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.toAuthErrorMessage())
            }
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

    private fun Exception.toAuthErrorMessage(): String {
        val message = this.message.orEmpty()

        return when {
            message.contains("email address is badly formatted", ignoreCase = true) ->
                "Format email tidak valid."

            message.contains("password is invalid", ignoreCase = true) ->
                "Password salah."

            message.contains("no user record", ignoreCase = true) ->
                "Akun tidak ditemukan."

            message.contains("email address is already in use", ignoreCase = true) ->
                "Email sudah terdaftar."

            message.contains("password should be at least 6 characters", ignoreCase = true) ->
                "Password minimal 6 karakter."

            message.contains("network error", ignoreCase = true) ->
                "Koneksi bermasalah. Periksa internet kamu."

            else ->
                message.ifBlank {
                    "Terjadi kesalahan. Silakan coba lagi."
                }
        }
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