package com.dicoding.tugas_akhir.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.dicoding.tugas_akhir.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun signInWithGoogle(
    context: Context,
    auth: FirebaseAuth,
    scope: CoroutineScope,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(context.getString(R.string.default_web_client_id))
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    scope.launch {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential

            if (
                credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val firebaseCredential = GoogleAuthProvider.getCredential(
                        googleIdTokenCredential.idToken,
                        null
                    )

                    auth.signInWithCredential(firebaseCredential)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onError(exception.message ?: "Login Google gagal")
                        }
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("GoogleAuth", "Invalid Google ID token", e)
                    onError("Token Google tidak valid")
                }
            } else {
                onError("Credential Google tidak dikenali")
            }
        } catch (e: GetCredentialException) {
            Log.e("GoogleAuth", "Credential error", e)
            onError(e.message ?: "Login Google dibatalkan")
        }
    }
}