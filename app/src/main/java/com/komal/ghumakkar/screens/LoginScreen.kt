package com.komal.ghumakkar.screens

import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.komal.ghumakkar.auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = AuthViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    // Google Sign-in launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val credential = Identity.getSignInClient(context)
                .getSignInCredentialFromIntent(result.data)

            val token = credential.googleIdToken

            if (token != null) {
                viewModel.authenticateWithGoogle(token) { success ->
                    if (success) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            message = e.message ?: "Google Sign-in failed"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Login", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.loginWithEmail(email, password) { success, error ->
                    if (success) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else message = error ?: "Login failed"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                scope.launch {
                    val intent = viewModel.beginGoogleSignIn(context as android.app.Activity)
                    if (intent != null) {
                        launcher.launch(IntentSenderRequest.Builder(intent).build())
                    } else {
                        message = "Google sign-in not available"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }

        Spacer(Modifier.height(20.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Create new account")
        }

        if (message.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text(message, color = MaterialTheme.colorScheme.error)
        }
    }
}
