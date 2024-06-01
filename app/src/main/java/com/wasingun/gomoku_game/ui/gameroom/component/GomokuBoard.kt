package com.wasingun.gomoku_game.ui.gameroom.component

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wasingun.gomoku_game.R
import com.wasingun.gomoku_game.data.Stone
import com.wasingun.gomoku_game.ui.gameroom.OfflineGameViewModel

@Composable
fun GomokuBoard() {
    val offlineViewModel: OfflineGameViewModel = hiltViewModel()
    val boardSize = 14  // 15x15 grid
    val stoneRadius = 24f
    val configuration = LocalConfiguration.current
    val screenWidth = (configuration.screenWidthDp * 0.85).dp
    val context = LocalContext.current

    val assistantCircleUiState by
    offlineViewModel.assistantCircle.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    val stonesUiState by
    offlineViewModel.stones.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier
            .size(screenWidth)
            .align(Alignment.Center)
        ) {
            val cellSize = size.width / (boardSize)
            initBoardColor(context)
            initDrawingGrid(boardSize, cellSize)
            initMiddleCircle()
            initAssistantCircle(assistantCircleUiState, cellSize, stoneRadius)
            initDrawingStone(stonesUiState, cellSize, stoneRadius)
        }
    }
}

private fun DrawScope.initBoardColor(context: Context) {
    drawRect(
        color = Color(ContextCompat.getColor(context, R.color.yellow_700)),
        topLeft = Offset.Zero,
        size = size
    )
}

private fun DrawScope.initMiddleCircle() {
    drawCircle(
        color = Color.Black,
        center = Offset(size.width / 2, size.height / 2),
        radius = 6f
    )
}

private fun DrawScope.initAssistantCircle(assistantCircle: Pair<Int, Int>?, cellSize: Float, stoneRadius: Float) {
    if (assistantCircle != null) {
        drawCircle(
            color = Color.Red,
            center = Offset(assistantCircle.first * cellSize, assistantCircle.second * cellSize),
            radius = stoneRadius,
            style = Stroke(width = 3f)
        )
    }
}

private fun DrawScope.initDrawingGrid(boardSize: Int, cellSize: Float) {
    for (i in 0 until boardSize + 1) {
        val position = cellSize * i
        drawLine(
            start = Offset(position, 0f),
            end = Offset(position, size.height),
            color = Color.Gray,
            strokeWidth = 3f
        )
        drawLine(
            start = Offset(0f, position),
            end = Offset(size.width, position),
            color = Color.Gray,
            strokeWidth = 3f
        )
    }
}

private fun DrawScope.initDrawingStone(stones: List<Stone>, cellSize: Float, stoneRadius: Float) {
    stones.forEach { stone ->
        if (stone.color == 1) {
            drawCircle(
                color = Color.Black,
                center = Offset(stone.x * cellSize, stone.y * cellSize),
                radius = stoneRadius
            )
        } else {
            drawCircle(
                color = Color.Black,
                center = Offset(stone.x * cellSize, stone.y * cellSize),
                radius = stoneRadius
            )

            drawCircle(
                color = Color.White,
                center = Offset(stone.x * cellSize, stone.y * cellSize),
                radius = stoneRadius - 2f
            )
        }
    }
}