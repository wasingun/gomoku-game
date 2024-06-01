package com.wasingun.gomoku_game.ui.gameroom.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun WinningMessage(
    message : String,
    modifier: Modifier,
) {
    Text(
        text = message,
        modifier = modifier,
        fontSize = 40.sp,
        style = TextStyle(color = androidx.compose.ui.graphics.Color.Black)
    )
}