package com.wasingun.gomoku_game.ui.gameroom

import androidx.lifecycle.ViewModel
import com.wasingun.gomoku_game.R
import com.wasingun.gomoku_game.data.Stone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OfflineGameViewModel @Inject constructor() : ViewModel() {

    private var _assistantCircle = MutableStateFlow<Pair<Int, Int>?>(null)
    val assistantCircle: StateFlow<Pair<Int, Int>?> get() = _assistantCircle

    private var _stones = MutableStateFlow<List<Stone>>(listOf())
    val stones: StateFlow<List<Stone>> get() = _stones

    private var _isWin = MutableStateFlow(0)
    val isWin : StateFlow<Int> get() = _isWin

    private var _threeThreeMessage = MutableStateFlow(false)
    val threeThreeMessage : StateFlow<Boolean> get() = _threeThreeMessage

    private var board = Array(15) { IntArray(15) }
    private val winLength = 5
    private val boardSize = 15

    fun tapAssistantCircle(tapLocation: Pair<Int, Int>) {
        if (isWin.value == 0) {
            _assistantCircle.value = tapLocation
        }
    }

    fun putStone(stone: Stone) {
        if (board[stone.x][stone.y] == 0 && checkThreeThree(stone)) {
            _stones.value += stone

            board[stone.x][stone.y] = stone.color
            _assistantCircle.value = null

            if (checkWin(stone.x, stone.y, stone.color)) {
                board = Array(15) { IntArray(15) }
                _isWin.value = stone.color
            }
        }
    }

    fun resetGame() {
        board = Array(15) { IntArray(15) }
        _stones.value = listOf()
        _isWin.value = 0
    }

    private fun checkWin(x: Int, y: Int, color: Int): Boolean {
        val directions = listOf(
            Pair(1, 0), Pair(0, 1), Pair(1, 1), Pair(1, -1)
        )

        for (direction in directions) {
            var count = 1

            count += countFiveStone(x, y, direction.first, direction.second, color)
            count += countFiveStone(x, y, -direction.first, -direction.second, color)

            if (count > winLength) {
                if (color == 2) {
                    return true
                } else {
                    return false
                }
            } else if (count == winLength) {
                return true
            }
        }
        return false
    }

    private fun countFiveStone(
        x: Int,
        y: Int,
        xDirection: Int,
        yDirection: Int,
        stoneColor: Int
    ): Int {
        var count = 0
        var nextX = x + xDirection
        var nextY = y + yDirection

        while (
            nextX in 0 until boardSize &&
            nextY in 0 until boardSize &&
            board[nextX][nextY] == stoneColor
        ) {
            count++
            nextX += xDirection
            nextY += yDirection
        }
        return count
    }

    fun checkThreeThree(stone: Stone): Boolean {
        board[stone.x][stone.y] = stone.color

        val directionList1 = mutableListOf<List<Int>>()
        val directionList2 = mutableListOf<List<Int>>()
        val directionList3 = mutableListOf<List<Int>>()
        val directionList4 = mutableListOf<List<Int>>()
        val allDirectionList = listOf(directionList1, directionList2, directionList3, directionList4)
        val checkOpenThreeList1 = listOf(listOf(1, 0, 1, 1), listOf(1, 1, 0, 1),)
        var openThreeCount = 0

        getAllDirectionsStone(
            stone,
            checkOpenThreeList1,
            directionList1,
            directionList2,
            directionList3,
            directionList4
        )
        board[stone.x][stone.y] = 0

        openThreeCount = countUpOpenThree(allDirectionList)

        if (openThreeCount >= 2) {
            setViolationMessage()
            return false
        } else {
            return true
        }
    }

    private fun setViolationMessage() {
        _threeThreeMessage.value = true
    }

    fun resetViolationMessage() {
        _threeThreeMessage.value = false
    }

    private fun getAllDirectionsStone(
        stone: Stone,
        checkOpenThreeList1: List<List<Int>>,
        directionList1: MutableList<List<Int>>,
        directionList2: MutableList<List<Int>>,
        directionList3: MutableList<List<Int>>,
        directionList4: MutableList<List<Int>>
    ) {
        for (count in 3 downTo 0) {
            val x = stone.x + count
            val y = stone.y
            if (x in 0..14 &&
                y in 0..14 &&
                x - 3 in 0..14
            ) {
                val stoneCase = listOf(board[x][y], board[x - 1][y], board[x - 2][y], board[x - 3][y])
                if (checkOpenThreeList1.contains(stoneCase)) {
                    if (board[x + 1][y] in 0..14 &&
                        board[x - 4][y] in 0..14 &&
                        board[x + 1][y] == 0 &&
                        board[x - 4][y] == 0
                    ) {
                        directionList1.add(stoneCase)
                    }
                } else {
                    directionList1.add(stoneCase)
                }
            }
        }

        for (count in 3 downTo 0) {
            val x = stone.x
            val y = stone.y + count
            if (x in 0..14 &&
                y in 0..14 &&
                y - 3 in 0..14
            ) {
                val stoneCase = listOf(board[x][y], board[x][y - 1], board[x][y - 2], board[x][y - 3])
                if (checkOpenThreeList1.contains(stoneCase)) {
                    if (board[x][y + 1] in 0..14 &&
                        board[x][y - 4] in 0..14 &&
                        board[x][y + 1] == 0 &&
                        board[x][y - 4] == 0
                    ) {
                        directionList2.add(stoneCase)
                    }
                } else {
                    directionList2.add(stoneCase)
                }
            }
        }

        for (count in 3 downTo 0) {
            val x = stone.x + count
            val y = stone.y + count
            if (x in 0..14 &&
                y in 0..14 &&
                x - 3 in 0..14 &&
                y - 3 in 0..14
            ) {
                val stoneCase = listOf(board[x][y], board[x - 1][y - 1], board[x - 2][y - 2], board[x - 3][y - 3])
                if (checkOpenThreeList1.contains(stoneCase)) {
                    if (board[x + 1][y + 1] in 0..14 &&
                        board[x - 4][y - 4] in 0..14 &&
                        board[x + 1][y + 1] == 0 &&
                        board[x - 4][y - 4] == 0
                    ) {
                        directionList3.add(stoneCase)
                    }
                } else {
                    directionList3.add(stoneCase)
                }
            }
        }

        for (count in 3 downTo 0) {
            val x = stone.x + count
            val y = stone.y - count
            if (x in 0..14 &&
                y in 0..14 &&
                x - 3 in 0..14 &&
                y + 3 in 0..14
            ) {
                val stoneCase = listOf(board[x][y], board[x - 1][y + 1], board[x - 2][y + 2], board[x - 3][y + 3])
                if (checkOpenThreeList1.contains(stoneCase)) {
                    if (board[x - 4][y + 4] in 0..14 &&
                        board[x + 1][y - 1] in 0..14 &&
                        board[x - 4][y + 4] == 0 &&
                        board[x + 1][y - 1] == 0
                    ) {
                        directionList4.add(stoneCase)
                    }
                } else {
                    directionList4.add(stoneCase)
                }
            }
        }
    }

    private fun countUpOpenThree(allDirectionList: List<MutableList<List<Int>>>) : Int {
        var openThreeCount = 0
        for (directionList in allDirectionList) {
            if (directionList.contains(listOf(1, 1, 1, 0)) && directionList.contains(listOf(0, 1, 1, 1)) && !directionList.contains(listOf(1, 1, 1, 1))) {
                openThreeCount += 1
            }
            if (directionList.contains(listOf(1, 0, 1, 1)) || directionList.contains(listOf(1, 1, 0, 1))) {
                openThreeCount += 1
            }
        }
        return openThreeCount
    }
}