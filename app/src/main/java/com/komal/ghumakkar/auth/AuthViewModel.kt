package com.komal.ghumakkar.auth

import android.app.Activity
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // EMAIL LOGIN
    fun loginWithEmail(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                onResult(it.isSuccessful, it.exception?.message)
            }
    }

    // EMAIL REGISTER
    fun registerWithEmail(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                onResult(it.isSuccessful, it.exception?.message)
            }
    }

    // GOOGLE SIGN-IN
    suspend fun beginGoogleSignIn(activity: Activity): IntentSender? {
        val oneTapClient = Identity.getSignInClient(activity)

        val signInRequest = BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("890884682111-mt75kmsj7s819tvst983hj8curr05vnt.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        return try {
            val result = oneTapClient.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            null
        }
    }

    fun authenticateWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun currentUser() = auth.currentUser
}
