package com.wasingun.gomoku_game.ui.gameroom.component

import android.content.Context
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.wasingun.gomoku_game.R

@Composable
fun PutStoneButton(
    click: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    reversed: Boolean
) {
    Button(
        onClick = { click() },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color(ContextCompat.getColor(LocalContext.current, R.color.indigo_500)),
        )
    ) {
        Text(text,
            fontSize = 20.sp,
            color = Color.White,
            modifier = if(reversed) Modifier else Modifier.graphicsLayer(scaleY = -1f, scaleX = -1f))
    }
}

@Composable
fun ResetButton(
    click: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    context: Context
) {
    Button(
        onClick = { click() },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color(ContextCompat.getColor(context, R.color.yellow_700)),
            containerColor = Color(ContextCompat.getColor(context, R.color.yellow_700)),
        )
    ) {
        Text(text,
            fontSize = 20.sp,
            color = Color.Black)
    }
}