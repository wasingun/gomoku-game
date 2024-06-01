package com.wasingun.gomoku_game.ui.gameroom

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wasingun.gomoku_game.R
import com.wasingun.gomoku_game.data.Stone
import com.wasingun.gomoku_game.ui.gameroom.component.GomokuBoard
import com.wasingun.gomoku_game.ui.gameroom.component.PutStoneButton
import com.wasingun.gomoku_game.ui.gameroom.component.ResetButton
import com.wasingun.gomoku_game.ui.gameroom.component.WinningMessage
import kotlin.math.roundToInt

@Composable
fun GameRoom() {
    val boardSize = 14  // 15x15 grid
    val configuration = LocalConfiguration.current
    val screenWidth = (configuration.screenWidthDp * 0.85).dp
    val context = LocalContext.current
    val viewModel: OfflineGameViewModel = hiltViewModel()
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current

    val stonesUiState by
    viewModel.stones.collectAsStateWithLifecycle(lifecycleOwner)

    val assistantCircleUiState by
    viewModel.assistantCircle.collectAsStateWithLifecycle(lifecycleOwner)

    val isWin by
    viewModel.isWin.collectAsStateWithLifecycle(lifecycleOwner)

    val isThreeThree by
    viewModel.threeThreeMessage.collectAsStateWithLifecycle(lifecycleOwner)

    if (isThreeThree) {
        val message = context.getString(R.string.violation_three_three)
        Toast.makeText(context, message , Toast.LENGTH_SHORT).show()
        viewModel.resetViolationMessage()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(ContextCompat.getColor(context, R.color.yellow_200)))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                PutStoneButton(
                    click = {
                        putStone(assistantCircleUiState, stonesUiState, viewModel)
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .align(Alignment.CenterHorizontally),
                    text = context.getString(R.string.placement),
                    enabled = stonesUiState.size % 2 != 0,
                    reversed = false
                )
            }
        }

        Box(modifier = Modifier
            .size(screenWidth / 14 * 16)
            .align(Alignment.Center)
            .border(2.dp, Color.Black)
            .background(color = Color(ContextCompat.getColor(context, R.color.yellow_700)))
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cellSize = size.width / (boardSize + 2)
                    var x = (offset.x / cellSize).roundToInt() - 1
                    var y = (offset.y / cellSize).roundToInt() - 1
                    when (x) {
                        -1 -> x = 0
                        15 -> x = 14
                    }
                    when (y) {
                        -1 -> y = 0
                        15 -> y = 14
                    }
                    viewModel.tapAssistantCircle(Pair(x, y))
                }
            }
        )

        GomokuBoard()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterEnd)
                .padding(10.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {

                when (isWin) {
                    1 -> WinningMessage(
                        message = stringResource(R.string.black_win),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    2 -> WinningMessage(
                        message = stringResource(R.string.white_win),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                if (isWin != 0) {
                    ResetButton(
                        click = { viewModel.resetGame() },
                        text = stringResource(R.string.restart_game),
                        context = context,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(150.dp)
                    )
                }

                Spacer(modifier = Modifier.size(20.dp))

                PutStoneButton(
                    click = { putStone(assistantCircleUiState, stonesUiState, viewModel) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(150.dp),
                    text = context.getString(R.string.placement),
                    enabled = stonesUiState.size % 2 == 0,
                    reversed = true
                )
            }
        }
    }
}

private fun putStone(assistantCircleUiState: Pair<Int, Int>?, stonesUiState: List<Stone>, viewModel: OfflineGameViewModel) {
    assistantCircleUiState?.let {
        val stone = Stone(it.first, it.second, isBlack(stonesUiState))
        viewModel.putStone(stone)
    }
}

private fun isBlack(stones: List<Stone>): Int {
    return when {
        stones.size % 2 == 0 -> 1
        else -> 2
    }
}