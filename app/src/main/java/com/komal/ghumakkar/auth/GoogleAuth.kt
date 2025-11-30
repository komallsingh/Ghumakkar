package com.komal.ghumakkar.auth


import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuth(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()

    private val oneTapClient = Identity.getSignInClient(context)

    private val signInRequest = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("890884682111-mt75kmsj7s819tvst983hj8curr05vnt.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    suspend fun signIn(): String? {
        return try {
            val result = oneTapClient.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender.toString()
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Sign-In Error: ${e.message}")
            null
        }
    }

    fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }
}
