package com.example.ucp2.ui.view.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.scale
import com.example.ucp2.ui.customWidget.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

@Composable
fun HomeScreen(
    onBarangClick: () -> Unit,
    onSuplierClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State for animation control
    var isVisible by remember { mutableStateOf(false) }

    // Trigger animation when the view is loaded
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)) //
    ) {
        // Include the TopAppBar at the top
        TopAppBar(
            onBack = {}, // No back action in HomeView
            showBackButton = false,
            judul = "Home",
            backgroundColor = Color(0xFF6200EA),
            contentColor = Color.White // White text and icon
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Buttons with animations
            AnimatedButton(text = "Barang", icon = Icons.Filled.Person, onClick = onBarangClick)
            AnimatedButton(text = "Suplier", icon = Icons.Filled.Edit, onClick = onSuplierClick)
        }
    }
}

@Composable
fun AnimatedButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6200EA), // Matching primary purple
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 12.dp) // Larger padding for a bigger button
            .height(90.dp) // Increase button height
            .fillMaxWidth(0.9f) // Make button wider
            .clickable { isPressed = !isPressed }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon with increased size
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp) // Padding between icon and text
                    .size(50.dp), // Increased icon size
                tint = Color.White
            )
            // Text
            Text(
                text = text,
                fontSize = 30.sp, // Increase font size for bigger text
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .animateScale(isPressed) // Adding animation to text
            )
        }
    }
}

@Composable
fun Modifier.animateScale(isPressed: Boolean): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1.0f,
        animationSpec = tween(durationMillis = 300)
    )
    return this.then(Modifier.scale(scale))
}