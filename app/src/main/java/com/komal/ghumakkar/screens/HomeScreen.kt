package com.komal.ghumakkar.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen() {
    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Welcome Home!", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(10.dp))

        Text("Logged in as:")
        Text(
            text = user?.email ?: user?.displayName ?: "Unknown User",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(20.dp))

        Text("This is your simple home screen.")
    }
}
