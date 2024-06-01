package com.wasingun.gomoku_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.wasingun.gomoku_game.ui.gameroom.GameRoom
import com.wasingun.gomoku_game.ui.gameroom.OfflineGameViewModel
import com.wasingun.gomoku_game.ui.theme.GomokugameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameRoom()

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GomokugameTheme {

    }
}