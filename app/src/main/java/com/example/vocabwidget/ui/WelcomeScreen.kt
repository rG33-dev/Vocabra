package com.example.vocabwidget.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocabwidget.ui.theme.BlueDark
import com.example.vocabwidget.ui.theme.BluePrimary

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(BlueDark, BluePrimary))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(40.dp)) {
            Icon(Icons.Default.Public, null, tint = Color.White, modifier = Modifier.size(72.dp))
            Spacer(Modifier.height(16.dp))
            Text(
                text = "VOCAB",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 8.sp
                )
            )
            Text("Master language with precision.", color = Color.White.copy(alpha = 0.7f))
            Spacer(Modifier.height(64.dp))
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(56.dp).fillMaxWidth()
            ) {
                Text("GET STARTED", color = BluePrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
