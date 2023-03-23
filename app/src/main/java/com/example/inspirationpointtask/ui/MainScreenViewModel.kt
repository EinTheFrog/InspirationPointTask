package com.example.inspirationpointtask.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TableValues(val cells: Array<Array<String>>)

class MainScreenViewModel: ViewModel() {
    val tableState = MutableStateFlow(
        TableValues(Array(7) { Array(7) { "" } })
    )

    fun updateTable(row: Int, column: Int, value: String) {
        tableState.update {
            val newCells = tableState.value.cells
            newCells[row][column] = value
            return@update TableValues(newCells)
        }
    }

    fun calculateRowScore(row: Int): Int {
        var sum = 0
        for (cellValue in tableState.value.cells[row]) {
            if (valueIsAllowed(cellValue)) {
                sum += cellValue.toInt()
            }
        }
        return sum
    }

    fun calculateRowPlace(row: Int): Int {
        val rowScore = calculateRowScore(row)
        var place = 1
        for (i in 0..6) {
            val score = calculateRowScore(i)
            if (score > rowScore || score == rowScore && i < row) place++
        }
        return place
    }

}

fun valueIsAllowed(value: String): Boolean = value.isNotEmpty() && value.isDigitsOnly() && value.toInt() <= 5