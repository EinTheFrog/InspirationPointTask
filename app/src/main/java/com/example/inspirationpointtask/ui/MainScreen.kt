package com.example.inspirationpointtask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.inspirationpointtask.ui.theme.InspirationPointTaskTheme

@Composable
fun MainScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        for (i in 0..6) {
            Row(modifier = Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.CenterVertically) {
                for (j in 0..6) {
                    Text(modifier = Modifier.weight(1f), textAlign = TextAlign.Center, text ="$i.$j")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InspirationPointTaskTheme {
        MainScreen()
    }
}