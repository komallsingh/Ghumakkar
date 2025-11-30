package com.komal.ghumakkar.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.komal.ghumakkar.R
import kotlinx.coroutines.delay

val GhumakkarFont = FontFamily(
    Font(R.font.italian)
)

@Composable
fun SplashScreen(navController: NavController) {

    // Fade-in/fade-out animation for "Welcome to"
    val welcomeAlpha = remember { Animatable(0f) }

    LaunchedEffect(true) {
        welcomeAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        delay(1000)
        welcomeAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image with subtle gradient overlay
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x66000000),
                                Color(0x00000000),
                                Color(0x66000000)
                            )
                        )
                    )
                },
            contentScale = ContentScale.Crop
        )

        // Text layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "Welcome to" - Bold
            Text(
                text = "Welcome to",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(welcomeAlpha.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // "Ghumakkar" - cursive with typing animation
            TypeWriterText(
                text = "Ghumakkar",
                textStyle = TextStyle(
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontFamily = GhumakkarFont
                ),
                delayPerChar = 150L
            )
        }
    }

    // Navigate to Login screen after animation
    LaunchedEffect(true) {
        val totalDelay = 1500 + 1000 + (150L * "Ghumakkar".length)
        delay(totalDelay)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }
}

@Composable
fun TypeWriterText(
    text: String,
    textStyle: TextStyle,
    delayPerChar: Long = 100L
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        displayedText = ""
        for (char in text) {
            displayedText += char
            delay(delayPerChar)
        }
    }

    Text(
        text = displayedText,
        style = textStyle
    )
}
